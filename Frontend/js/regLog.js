function togglePassword(fieldID, btn) {
    const input = document.getElementById(fieldID);

    if (input.type === "password") {
        input.type = "text";
        btn.textcontent = "🙈"
    }
    else {
        input.type = "password";
        btn.textcontent = "👁️";
    }
}