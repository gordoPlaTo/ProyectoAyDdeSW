const uploadPhoto = document.getElementById("uploadPic");
const userPhoto = document.getElementById("userPic");

if (uploadPhoto && userPhoto) {
    uploadPhoto.addEventListener("change", (e) => {
        const file = e.target.files[0];
        if (file && file.type.startsWith("image/")) {
            const reader = new FileReader();
            reader.onload = (event) => {
                userPhoto.src = event.target.result;
            };
            reader.readAsDataURL(file);
        } else {
            alert("Por favor, selecciona un formato de imagen válido.");
        }
    })

}