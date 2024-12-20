$(document).ready(function () {
    $("#submitBtn").click(function () {
        var fName = $("#fName").val();
        var lName = $("#lName").val();
        var email = $("#email").val();
        var password = $("#password").val();
        var data = {
            fName: fName,
            lName: lName,
            email: email,
            password: password
        };

        $.ajax({
            url: "/bin/newsportal/users",
            method: "POST",
            contentType: "application/json", 
            data: JSON.stringify(data), 
            success: function (response) {
                alert("User saved successfully!");
            },
            error: function (xhr, status, error) {
                alert("Error saving user: " + xhr.status + " - " + error);
            }
        });
    });
});