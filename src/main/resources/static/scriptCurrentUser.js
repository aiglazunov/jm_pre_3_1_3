getCurrent();

function getCurrent() {
    $.ajax({
        url: "api/users/getCurrent",
        type: "GET",
        dataType: "json"
    }).done((msg) => {
        let user = JSON.parse(JSON.stringify(msg));
        //console.log(user);
        let rolesStr = new Array();
        $.each(user.roles, (i, role) =>{
            rolesStr.push(role.role.replace('ROLE_',''));
        })


        $("#user-table tbody").empty().append(
            $("<tr>").append(
                $("<td>").text(user.id),
                $("<td>").text(user.name),
                $("<td>").text(user.lastName),
                $("<td>").text(user.age),
                $("<td>").text(user.username),
                $("<td>").text(rolesStr.join(' '))
            ));
    }).fail(() => {
        alert("Error get current user")
    })
}