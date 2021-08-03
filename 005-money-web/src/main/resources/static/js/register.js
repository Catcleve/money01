$(function () {
//错误提示
  function showError(id, msg) {
    $("#" + id + "Ok").hide();
    $("#" + id + "Err").html("<i></i><p>" + msg + "</p>");
    $("#" + id + "Err").show();
    $("#" + id).addClass("input-red");
  }

//错误隐藏
  function hideError(id) {
    $("#" + id + "Err").hide();
    $("#" + id + "Err").html("");
    $("#" + id).removeClass("input-red");
  }

//显示成功
  function showSuccess(id) {
    $("#" + id + "Err").hide();
    $("#" + id + "Err").html("");
    $("#" + id + "Ok").show();
    $("#" + id).removeClass("input-red");
  }


//打开注册协议弹层
  /*function alertBox(maskid, bosid) {
    $("#" + maskid).show();
    $("#" + bosid).show();
  }*/

  function alertBox(maskid, bosid) {
    alert("同意就行")
  }

//关闭注册协议弹层
  function closeBox(maskid, bosid) {
    $("#" + maskid).hide();
    $("#" + bosid).hide();
  }

//注册协议确认
  $(function () {
    $("#agree").click(function () {
      var ischeck = document.getElementById("agree").checked;
      if (ischeck) {
        $("#btnRegist").attr("disabled", false);
        $("#btnRegist").removeClass("fail");
      } else {
        $("#btnRegist").attr("disabled", "disabled");
        $("#btnRegist").addClass("fail");
      }
    });
  });

  //一个标记，默认格式错误
  let flag = false;
  let $phone = $("#phone");
  let $loginPassword = $("#loginPassword");
  //手机号验证
  $phone.blur(function () {
    flag = false;
    //获取用户输入的手机号，去除头尾空格
    let phone = $(this).val().trim();

    //验证手机号格式
    if (!/^1[1-9]\d{9}$/.test(phone)) {
      showError("phone", "请输入正确的手机号");
      return;
    }
    //长度必须是11位
    if (phone.length !== 11) {
      showError("phone", "请输入正确的手机号");
      return;
    }

    //后台验证是否重复
    $.get("/web/loan/page/checkRegisterPhone",
        {phone: phone},
        function (data) {
          if (data.code !== 1) {
            showError("phone", data.message);
          } else {
            //  可以在这里写注册，能够解决再次修改手机号码的问题
          }
        },
        'json');
    showSuccess("phone");
    flag = true;
  })


//  密码验证
  $loginPassword.blur(function () {
    flag = false;
    //获取用户输入的手机号，去除头尾空格
    let loginPassword = $(this).val().trim();

    // 密码字符只可使用数字和大小写英文字母
    if (!/^[0-9a-zA-Z]+$/.test(loginPassword)) {
      showError("loginPassword", "密码字符只可使用数字和大小写英文字母");
      return;
    }
    //密码应同时包含英文和数字
    if (!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(loginPassword)) {
      showError("loginPassword", "密码应同时包含英文和数字");
      return;
    }

    //密码长度必须大于6位
    if (loginPassword.length < 6) {
      showError("loginPassword", "密码长度必须大于6位");
    }

    showSuccess("loginPassword");
    flag = true
  })

  //点击注册按钮
  $("#btnRegist").click(function () {
    $phone.blur()
    $loginPassword.blur()


    //可以注册
    if (flag) {
      $.post("",
          {
            phone: $phone.val(),
            loginPassword: $loginPassword.val()
          },

          function (data) {

          },
          'json');
    }

  })

})


