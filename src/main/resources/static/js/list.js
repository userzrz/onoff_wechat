$(function () {
    //点击按钮显示隐藏div
    $("#but_history").click(function () {
        $(".dropdown-menu1").toggle();
    });

    //点击按钮显示隐藏div
    $("#but_history2").click(function () {
        $(".dropdown-menu2").toggle();
    });

    //提交请求
    $(".dropdown-item").click(function () {
        var period = $(this).text();
        if (period == '本周') {
            var code=$("#a_1").attr("title");
            var url = "/week/" + code;
            console.log(url)
        }else if(period == '本月'){
            var code=$("#a_1").attr("title");
            var url = "/week/" + code+"month";
        } else if(period=='上月'){
            var url = "/week/" + "month";
        }else{
            var url = "/week/" +$(this).attr("id")+"weeks";
            console.log(url)
        }
        $.ajax({
            url: url,
            type: 'GET',
            success: function (data) {
                $("#sup-sup").html(data);
            }
        })
        $(".dropdown-menu1").hide();
        $(".dropdown-menu2").hide();
    });

});

