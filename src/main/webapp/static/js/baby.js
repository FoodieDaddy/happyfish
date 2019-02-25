$(function(){

	//点击返回主页
	$('.back').click(function(){
		window.location.href="/static/index.html";
	})

	//点击金币显示充值页面
	$('.b-money').click(function(){
		$('.pay').show()
	})
	//充值数字按钮
	$('.pay-list li').click(function(){
		$('.pay-list li').css('background','url(./img/btn_pay2.png) no-repeat').css('background-size','100% 100%');
		$(this).css('background','url(./img/btn_pay1.png) no-repeat').css('background-size','100% 100%');
	})

	//支付关闭
	$('.pay-close').click(function(){
		$('.pay').hide();
		$('.list li').find('.active2').removeClass('active')
		$('.list li').find('.no-active').removeClass('no-active1')
		$('.list li').eq(0).find('.active2').addClass('active')
		$('.list li').eq(0).find('.no-active').addClass('no-active1')
	})

	//玩法
	var type = 2; 
	$('.b-two').click(function(){
		type = 2;
		$(this).find('img').attr('src','./img/even2.png');
		$('.b-five').find('img').attr('src','./img/odd1.png');
		$(".guess-big-small").show();
		$(".guess-num").hide();
		$(".hand-money").text(10)
		$(".hash-money").text(20)
		$('.hand-num').text(1);
	})
	$('.b-five').click(function(){
		type=5;
		$(this).find('img').attr('src','./img/odd2.png');
		$('.b-two').find('img').attr('src','./img/even1.png');
		$(".guess-big-small").hide();
		$(".guess-num").show();
		$(".hand-money").text(10)
		$(".hash-money").text(50)
		$('.hand-num').text(1);
	})


	//猜大小按钮
	var guess_num = 1;
	$('.guess-list1 li').click(function(){
		$('.guess-list1 li').removeClass("b-active");
		$(this).addClass('b-active');
		guess_num = $(this).attr('num');
	})

	//猜数字按钮
	$('.guess-list2 li').click(function(){
		$('.guess-list2 li').removeClass("num-active");
		$(this).addClass('num-active');
		guess_num = $(this).text();
	})

	//最大按钮
	$(".b-max").click(function(){
		if(type==1){
			$(".hand-money").text(1000)
			$(".hash-money").text(2000)
		}else if(type==2){
			$(".hand-money").text(1000)
			$(".hash-money").text(5000)
		}
		$('.hand-num').text(100)
		
	})

	//最小按钮
	$(".b-min").click(function(){
		if(type==1){
			$(".hand-money").text(10)
			$(".hash-money").text(20)
		}else if(type==2){
			$(".hand-money").text(10)
			$(".hash-money").text(50)
		}
		$('.hand-num').text(1)
	})

	//减按钮
	$('.b-reduce').click(function(){
		let num = Number($(".hand-num").text())
		num--;
		if(num<=1){
			num=1
		}
		let hand_money = 10;
		let hash_money = 20;
		if(type==1){
			hand_money = 10 * num;
			hash_money = 20 * num;
		}else if(type==2){
			hand_money = 10 * num;
			hash_money = 50 * num;
		}
		
		$(".hand-money").text(hand_money)
		$(".hash-money").text(hash_money)
		$('.hand-num').text(num);
	})

	//加按钮
	$('.b-add').click(function(){
		let num = Number($(".hand-num").text())
		num+=2;
		if(num>=100){
			num=100
		}
		let hand_money = 10;
		let hash_money = 20;
		if(type==1){
			hand_money = 10 * num;
			hash_money = 20 * num;
		}else if(type==2){
			hand_money = 10 * num;
			hash_money = 50 * num;
		}
		$(".hand-money").text(hand_money)
		$(".hash-money").text(hash_money)
		$('.hand-num').text(num);
	})


	//游戏规则
	$(".b-rule").click(function(){
		$(".game-rule").show();
	})
	$('.rule-close').click(function(){
		$(".game-rule").hide();
	})

	//开始夺宝
	$(".b-start").click(function(){
		$('.result').show();
		let guessValue = $(".hand-num").text();
		$.ajax({
			url:"duobao_url",
			method:"post",
			data:{
				type:type,
				guess_num:guess_num,
				guess_value:guessValue,
			},
			success:function(data){
				$('.b-money-num').text(data.money) //金币余额
				$('.last-num').text(data.lastNum) //上期尾数
				$('.result-num').text(data.openNum) //开奖金额
				let orderNum = data.orderNum
				let len = orderNum.length
				let num = orderNum.slice(10,len-1)
				$('.order-num2').text('...'+num) //显示订单号
				$('.prize-num').text(orderNum.slice(len)) //显示订单尾数
			}
		})
	})
	$(".result-close").click(function(){
		$('.result').hide();
	})
})