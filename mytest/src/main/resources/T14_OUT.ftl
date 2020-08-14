
{
    size: ${size},
    userName:${user.userName},
    age:10,
    mac:"${Sign(size,user.userName)}"
}

<xml>
    <key>${size}</key>
    <arg>
        <nu1>
            ${user.userName}
        </nu1>
    </arg>
</xml>