package com.payflow.audit_service.service;

import com.payflow.audit_service.dto.DashboardSummaryResponse;
import com.payflow.audit_service.dto.InvestigationResponse;
import com.payflow.audit_service.dto.InvestigationSummaryDto;
import com.payflow.audit_service.dto.TimelineEventDto;
import com.payflow.audit_service.entity.DltEvent;
import com.payflow.audit_service.entity.PaymentAuditEvent;
import com.payflow.audit_service.repository.DltEventRepository;
import com.payflow.audit_service.repository.PaymentAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvestigationService {

    private final PaymentAuditRepository auditRepository;

    private final DltEventRepository dltRepository;

    public InvestigationResponse investigate(
            String paymentReference) {

        List<PaymentAuditEvent> auditEvents =
                auditRepository.findByPaymentReferenceOrderByReceivedAtAsc(
                        paymentReference);

        List<DltEvent> dltEvents =
                dltRepository.findByPaymentReference(
                        paymentReference);

        List<TimelineEventDto> timeline =
                new ArrayList<>();

        auditEvents.stream()
                .map(this::toTimelineEvent)
                .forEach(timeline::add);

        dltEvents.stream()
                .map(this::toTimelineEvent)
                .forEach(timeline::add);

        timeline.sort(Comparator.comparing(
                TimelineEventDto::getTimestamp,
                Comparator.nullsLast(String::compareTo)));

        String paymentStatus =
                resolvePaymentStatus(timeline);

        String investigationStatus =
                resolveInvestigationStatus(dltEvents);

        String rootCause =
                resolveRootCause(dltEvents, timeline);

        String suggestedAction =
                resolveSuggestedAction(dltEvents, timeline);

        String healthScore =
                resolveHealthScore(
                        paymentStatus,
                        investigationStatus);

        return InvestigationResponse.builder()
                .paymentReference(paymentReference)
                .paymentStatus(paymentStatus)
                .investigationStatus(investigationStatus)
                .rootCause(rootCause)
                .suggestedAction(suggestedAction)
                .healthScore(healthScore)
                .investigationSummary(
                        resolveInvestigationSummary(
                                paymentStatus,
                                investigationStatus,
                                rootCause,
                                suggestedAction))
                .totalEvents(auditEvents.size())
                .totalDltEvents(dltEvents.size())
                .timeline(timeline)
                .build();
    }

    private TimelineEventDto toTimelineEvent(
            PaymentAuditEvent event) {

        return TimelineEventDto.builder()
                .eventType(event.getEventType())
                .status(resolveAuditStatus(event))
                .timestamp(formatTimestamp(event.getReceivedAt()))
                .source("AUDIT")
                .build();
    }

    private TimelineEventDto toTimelineEvent(
            DltEvent event) {

        return TimelineEventDto.builder()
                .eventType("DLT")
                .status(event.getStatus())
                .description(event.getErrorReason())
                .timestamp(formatTimestamp(event.getReceivedAt()))
                .source("DLT")
                .build();
    }

    private String resolveAuditStatus(
            PaymentAuditEvent event) {

        if (event.getNewStatus() != null) {
            return event.getNewStatus();
        }

        return event.getStatus();
    }

    private String resolveCurrentStatus(
            List<TimelineEventDto> timeline) {

        return timeline.stream()
                .filter(event ->
                        !"DLT".equals(event.getSource()))
                .map(TimelineEventDto::getStatus)
                .filter(Objects::nonNull)
                .reduce((previous, current) -> current)
                .orElse("UNKNOWN");
    }

    private String formatTimestamp(
            LocalDateTime timestamp) {

        return timestamp == null
                ? null
                : timestamp.toString();
    }

    private String resolvePaymentStatus(List<TimelineEventDto> timeline) {

        return timeline.stream()
                .filter(event -> !"DLT".equals(event.getSource()))
                .map(TimelineEventDto::getStatus)
                .filter(Objects::nonNull)
                .reduce((previous, current) -> current)
                .orElse("UNKNOWN");
    }

    private String resolveInvestigationStatus(List<DltEvent> dltEvents) {

        if (dltEvents == null || dltEvents.isEmpty()) {
            return "HEALTHY";
        }

        boolean hasUnresolvedDlt =
                dltEvents.stream()
                        .anyMatch(event ->
                                "UNRESOLVED".equalsIgnoreCase(event.getStatus()));

        return hasUnresolvedDlt
                ? "ATTENTION_REQUIRED"
                : "RESOLVED";
    }

    private String resolveRootCause(
            List<DltEvent> dltEvents,
            List<TimelineEventDto> timeline) {
        if (dltEvents != null && !dltEvents.isEmpty()) {
            return dltEvents.stream()
                    .map(DltEvent::getErrorReason)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse("Technical processing failure");
        }
        boolean stuckDetected =
                timeline.stream()
                        .anyMatch(event ->
                                "PAYMENT_STUCK"
                                        .equals(event.getEventType()));
        if (stuckDetected) {
            return "Payment exceeded SLA threshold";
        }
        return "No operational issues detected";
    }

    private String resolveSuggestedAction(
            List<DltEvent> dltEvents,
            List<TimelineEventDto> timeline) {
        if (dltEvents != null && !dltEvents.isEmpty()) {
            return "Replay failed event from DLT topic";
        }
        boolean stuckDetected =
                timeline.stream()
                        .anyMatch(event ->
                                "PAYMENT_STUCK"
                                        .equals(event.getEventType()));
        if (stuckDetected) {
            return "Investigate payment processing workflow";
        }
        return "No action required";
    }
    private String resolveInvestigationSummary(
            String paymentStatus,
            String investigationStatus,
            String rootCause,
            String suggestedAction) {

        return String.format(
                "Payment status is %s. Investigation status is %s. Root cause identified as %s. Recommended action: %s.",
                paymentStatus,
                investigationStatus,
                rootCause,
                suggestedAction
        );
    }

    private String resolveHealthScore(
            String paymentStatus,
            String investigationStatus) {
        if ("ATTENTION_REQUIRED"
                .equalsIgnoreCase(investigationStatus)) {
            return "CRITICAL";
        }
        if ("STUCK"
                .equalsIgnoreCase(paymentStatus)) {
            return "WARNING";
        }
        return "HEALTHY";
    }

    public List<InvestigationSummaryDto> getAllInvestigations() {

        List<PaymentAuditEvent> auditEvents =
                auditRepository.findAll();

        List<DltEvent> dltEvents =
                dltRepository.findAll();

        Map<String, List<PaymentAuditEvent>> auditEventsByReference =
                auditEvents.stream()
                        .filter(event ->
                                event.getPaymentReference() != null)
                        .collect(Collectors.groupingBy(
                                PaymentAuditEvent::getPaymentReference));

        Map<String, List<DltEvent>> dltEventsByReference =
                dltEvents.stream()
                        .filter(event ->
                                event.getPaymentReference() != null)
                        .collect(Collectors.groupingBy(
                                DltEvent::getPaymentReference));

        Set<String> paymentReferences =
                new LinkedHashSet<>();

        paymentReferences.addAll(
                auditEventsByReference.keySet());
        paymentReferences.addAll(
                dltEventsByReference.keySet());

        return paymentReferences.stream()
                .map(paymentReference -> {

                    return buildInvestigationSummary(
                            paymentReference,
                            auditEventsByReference.getOrDefault(
                                    paymentReference,
                                    List.of()),
                            dltEventsByReference.getOrDefault(
                                    paymentReference,
                                    List.of()));
                })
                .toList();
    }

    private InvestigationSummaryDto buildInvestigationSummary(
            String paymentReference,
            List<PaymentAuditEvent> paymentEvents,
            List<DltEvent> paymentDltEvents) {

        String paymentStatus =
                resolvePaymentStatusFromAuditEvents(
                        paymentEvents);

        String investigationStatus =
                resolveInvestigationStatus(
                        paymentDltEvents);

        String healthScore =
                resolveHealthScore(
                        paymentStatus,
                        investigationStatus);

        return InvestigationSummaryDto.builder()
                .paymentReference(paymentReference)
                .paymentStatus(paymentStatus)
                .investigationStatus(investigationStatus)
                .healthScore(healthScore)
                .totalEvents(paymentEvents.size())
                .totalDltEvents(paymentDltEvents.size())
                .build();
    }

    private String resolvePaymentStatusFromAuditEvents(
            List<PaymentAuditEvent> paymentEvents) {

        return paymentEvents.stream()
                .sorted(Comparator.comparing(
                        PaymentAuditEvent::getReceivedAt,
                        Comparator.nullsLast(LocalDateTime::compareTo)))
                .map(this::resolveAuditStatus)
                .filter(Objects::nonNull)
                .reduce((previous, current) -> current)
                .orElse("UNKNOWN");
    }

    public DashboardSummaryResponse getDashboardSummary(){
        List<InvestigationSummaryDto> investigations =
                getAllInvestigations();
        long healthy =
                investigations.stream()
                        .filter(i ->
                                "HEALTHY".equals(i.getHealthScore()))
                        .count();

        long warning =
                investigations.stream()
                        .filter(i ->
                                "WARNING".equals(i.getHealthScore()))
                        .count();

        long critical =
                investigations.stream()
                        .filter(i ->
                                "CRITICAL".equals(i.getHealthScore()))
                        .count();


        return DashboardSummaryResponse.builder()
                .totalPayments(
                        (long) investigations.size())
                .healthyPayments(healthy)
                .warningPayments(warning)
                .criticalPayments(critical)
                .build();
    }

    public List<InvestigationSummaryDto> search(
            String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return getAllInvestigations();
        }

        String searchKeyword =
                keyword.trim();

        List<PaymentAuditEvent> auditEvents =
                auditRepository
                        .findByPaymentReferenceContainingIgnoreCase(
                                searchKeyword);

        List<DltEvent> dltEvents =
                dltRepository
                        .findByPaymentReferenceContainingIgnoreCase(
                                searchKeyword);

        Map<String, List<PaymentAuditEvent>> auditEventsByReference =
                auditEvents.stream()
                        .filter(event ->
                                event.getPaymentReference() != null)
                        .collect(Collectors.groupingBy(
                                PaymentAuditEvent::getPaymentReference));

        Map<String, List<DltEvent>> dltEventsByReference =
                dltEvents.stream()
                        .filter(event ->
                                event.getPaymentReference() != null)
                        .collect(Collectors.groupingBy(
                                DltEvent::getPaymentReference));

        Set<String> paymentReferences =
                new LinkedHashSet<>();

        paymentReferences.addAll(
                auditEventsByReference.keySet());
        paymentReferences.addAll(
                dltEventsByReference.keySet());

        return paymentReferences.stream()
                .map(paymentReference ->
                        buildInvestigationSummary(
                                paymentReference,
                                auditEventsByReference.getOrDefault(
                                        paymentReference,
                                        List.of()),
                                dltEventsByReference.getOrDefault(
                                        paymentReference,
                                        List.of())))
                .toList();
    }
}
