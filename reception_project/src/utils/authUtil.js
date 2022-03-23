export default {
  setAuthorization: (token) => localStorage.setItem("token", token),
  getAuthorization: () => localStorage.getItem("token"),
  removeAuthorization:() => localStorage.removeItem("token")
}

