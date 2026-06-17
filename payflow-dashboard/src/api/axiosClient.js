import axios from "axios";

const axiosClient = axios.create({
    baseURL: "http://localhost:8083",
});

export default axiosClient;