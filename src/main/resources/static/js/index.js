$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");

	// 获取标题和内容
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	// 发送异步请求
	$.post(
		CONTEXT_PATH + "/discuss/add",
		{"title":title, "content":content},
		function (data) {
			// data 是服务器返回回来的对象
			data = $.parseJSON(data);
			// 在提示框中显示返回消息
			$("hintBody").text(data.msg);
			// 显示提示框
			$("#hintModal").modal("show");
			// 2秒后，自动隐藏提示框
			setTimeout(function(){
				$("#hintModal").modal("hide");
				// 只有成功时，刷新页面
				if(data.code == 0) {
					window.location.reload();
				}
			}, 2000);

		}
	)


}