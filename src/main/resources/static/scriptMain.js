let allRoles;

getAllUsers();
getAllRoles();

function getAllRoles() {
    $.ajax({
        url: "api/users/roles",
        type: "GET",
        dataType: "json"
    }).done((data) => {
        allRoles = JSON.parse(JSON.stringify(data));

    })
}


// all user table
function getAllUsers() {
    $.ajax({
        url: "api/users",
        type: "GET",
        dataType: "json"
    }).done((msg) => {
        allUsers = JSON.parse(JSON.stringify(msg));
        $("#users-table tbody").empty();
        $.each(allUsers, (i, user) => {

            let rolesStr = new Array();
            $.each(user.roles, (i, role) => {
                rolesStr.push(role.role.replace('ROLE_', ''));
            })

            $("#users-table tbody").append(
                $("<tr>").append(
                    $("<td>").text(user.id),
                    $("<td>").text(user.name),
                    $("<td>").text(user.lastName),
                    $("<td>").text(user.age),
                    $("<td>").text(user.username),
                    $("<td>").text(rolesStr.join(' ')),

                    // mapim na knopki v index.html
                    $("<td>").append("<button type='button' data-toggle='modal' class='btn btn-info edit-btn'" +
                        "data-target='#editModal' data-user-id='" + user.id + "'>Edit</button>"),
                    $("<td>").append("<button type='button' data-toggle='modal' class='btn btn-danger delete-btn'" +
                        "data-target='#deleteModal' data-user-id='" + user.id + "'>Delete</button>")
                )
            );
        });
    });
}

// edit modal form
$("#editModal").on('show.bs.modal', (e) => {
    let userId = $(e.relatedTarget).data("user-id");
    console.log(userId)

    $.ajax({
        url: "api/users/" + userId,
        type: "GET",
        dataType: "json"
    }).done((data) => {
        let user = JSON.parse(JSON.stringify(data));
        //console.log(user);
        $("#id_edit").empty().val(user.id);
        $("#name_edit").empty().val(user.name);
        $("#lastname_edit").empty().val(user.lastName);
        $("#age_edit").empty().val(user.age);
        $("#username_edit").empty().val(user.username);
        $("#password_edit").empty().val(user.password);
        $("#roles_edit").empty();
        //console.log(allRoles);
        $.each(allRoles, (i, role) => {
            $("#roles_edit").append(
                $("<option>", {
                    value: role.id,
                    text: role.role
                })
                //.text(role.role).val(role.id)
            )
        });
        var userRoles = user.roles;
        //console.log(userRoles);
        var result = new Map(userRoles.map(i => [i.id, i.role]));
        var selRoles = Array.from(result.values());

        $.each(user.roles, (i, role) => {
            if (selRoles.includes("ROLE_USER")) {
                $('#roles_edit option[value=2]').prop('selected', true);
            }
            if (selRoles.includes("ROLE_ADMIN")) {
                $('#roles_edit option[value=1]').prop('selected', true);
            }

        });

        $("#buttonEditSubmit").on('click', (e) => {
            e.preventDefault();


            let editUser = {
                id: $("#id_edit").val(),
                name: $("#name_edit").val(),
                lastName: $("#lastname_edit").val(),
                password: $("#password_edit").val(),
                username: $("#username_edit").val(),
                age: $("#age_edit").val(),
                //roles: $("#roles_edit").empty()
                //roles: $("#roles_edit")
            }
            //console.log(editUser);

            editUser['roles'] = [];
            let temp = [];

            $("#roles_edit option").each(function () {
                if (this.selected) {
                    //console.log(this)
                    temp.push({id: this.value, role: this.text})
                    //editUser['roles'].push({id: this.value, role: this.text})
                }
            })

//            console.log(temp);
            editUser['roles'] = temp;
            console.log(editUser);


            $.ajax({
                url: "api/users/",
                type: "PUT",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify(editUser)
            }).done((msgUpdate) => {
                $("#editModal").modal('hide');
                getAllUsers();
            })

            $(this).unbind(e);


        })


    })
})


// delete modal form
$("#deleteModal").on('show.bs.modal', (e) => {
    let userId = $(e.relatedTarget).data("user-id");
    console.log(userId)

    $.ajax({
        url: "api/users/" + userId,
        type: "GET",
        dataType: "json"
    }).done((data) => {
        let user = JSON.parse(JSON.stringify(data));
        //console.log(user);
        $("#id_delete").empty().val(user.id);
        $("#name_delete").empty().val(user.name);
        $("#lastname_delete").empty().val(user.lastName);
        $("#age_delete").empty().val(user.age);
        $("#username_delete").empty().val(user.username);
        $("#password_delete").empty().val(user.password);
        $("#roles_delete").empty();
        //console.log(allRoles);
        $.each(allRoles, (i, role) => {
            $("#roles_delete").append(
                $("<option>", {
                    value: role.id,
                    text: role.role
                })
                //.text(role.role).val(role.id)
            )
        });

            $("#buttonDeleteSubmit").on('click', (e) => {
            e.preventDefault();

            let editUser = {
                id: $("#id_delete").val(),
                name: $("#name_delete").val(),
                lastName: $("#lastname_delete").val(),
                password: $("#password_delete").val(),
                username: $("#username_delete").val(),
                age: $("#age_delete").val()
                //roles: $("#roles_edit")
            }
            //console.log(editUser);

            editUser['roles'] = [];

            $("#roles_delete option").each(function () {
                if (this.selected) {
                    //console.log(this)
                    editUser['roles'].push({id: this.value, role: this.text})
                }
            })

            //console.log(editUser);

            $.ajax({
                url: "api/users/" + userId,
                type: "DELETE",
                dataType: "text"
            }).done((msgUpdate) => {
                $("#deleteModal").modal('hide');
                getAllUsers();
            })


        })


    })
})

// privyazka k knopke dobavleniya usera v html
$("#buttonNewSubmit").on('click', (e) => {
    e.preventDefault();

    let newUser = {
        id: $("#id_new").val(),
        name: $("#name_new").val(),
        lastName: $("#lastname_new").val(),
        password: $("#password_new").val(),
        username: $("#username_new").val(),
        age: $("#age_new").val()
        //roles: $("#roles_edit")
    }
    //console.log(editUser);

    newUser['roles'] = [];

    $("#allRoles_new option").each(function () {
        if (this.selected) {
            //console.log(this)
            newUser['roles'].push({id: this.value, role: this.text})
        }
    })

    console.log(newUser);


    $.ajax({
        url: "api/users",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify(newUser)
    }).done((msgSave) => {
        getAllUsers();
        $('#mainTab a[href="#home"]').tab('show');
        //$('#mainNav a[href="/user"]').tab('show');
    })


})