$(document).ready(function() {
    $(".deleteUser").on("click", function() {
        $("#userLoginHiddenInput").val($(this).attr("data-userLogin"));
        $("#deleteUserModal").modal();
    });

    $("#confirmDelete").on("click", function(event) {
        $("#userDeleteForm").submit();
    });
});