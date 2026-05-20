const API_URL = "http://localhost:8080/api";

export function getToken() {
    return localStorage.getItem("token");
}

export async function apiFetch(path, options = {}) {
    const token = getToken();

    const isFormData = options.body instanceof FormData;

    const response = await fetch(`${API_URL}${path}`, {
        ...options,

        headers: {
            ...(isFormData
                ? {}
                : { "Content-Type": "application/json" }),

            ...(token
                ? { Authorization: `Bearer ${token}` }
                : {}),

            ...options.headers
        }
    });

    if (!response.ok) {
        const text = await response.text();
        console.log(text);
        throw new Error(`Request failed with status ${response.status}`);
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}