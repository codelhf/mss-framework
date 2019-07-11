var loginInfo = {userId: 123456};
window.localStorage.setItem("login_token", JSON.stringify(loginInfo));
function getUserId() {
    return window.localStorage.getItem("login_token");
}