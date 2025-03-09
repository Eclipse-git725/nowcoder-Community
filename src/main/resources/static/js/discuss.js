function like(btn, entityType, entityId) {
    $.post (
        CONTEXT_PATH + "/like",
        {"entityType": entityType, "entityId": entityId},
        function (data) {
            data = $.parseJSON(data);
            if(data.code == 0) {
                // 更新点赞数量
                $(btn).children("i").text(data.likeCount);
                // 更新点赞状态
                $(btn).children("b").text(data.likeStatus == 1 ? '已赞' : '赞');
            } else {
                alert(data.msg);
            }
        }
    )

}