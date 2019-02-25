$(function(){
	var userId = ''
	let url = "https://easy-mock.com/mock/5c20e2910005940e5a746af2/test_copy"

	loadUserInfo();

	//用户一开始进来请求相关信息
	function loadUserInfo(){
		$.ajax({
			url:url+"/user/info",
			method:"get",
			success:function(data){
				console.log(data)
				const data2 = data.data
				userId = data2.id
				//用户id
				$('.user-id').text(data2.id)
				//用户充值的余额
				$('.pay-num').text(data2.money)
				//用户的佣金
				$('.yongjin-num').text(data2.yongjin)
			}
		})
	}
	
	//昨日排行榜
	function loadYesterdayRank(type){
		let str = ""
		if(type==1){
			//type=1是昨日排行榜
			$.ajax({
				url:url+"/user/rank",
				method:"get",
				success:function(data){
					console.log(data.data)
					const data1 = data.data
					if(data.code==1){
						for(let i=0;i<data1.list.length;i++){
							str += "<li>"+
										"<div>第"+(i+1)+"名</div>"+
										"<div>"+data1.list[i].id+"</div>"+
										"<div>"+data1.list[i].money+"</div>"+
									"</li>"
						}
						$('.rank-list').html(str)
					}
				}
			})
		}else{
			//type=2是总排行榜
			$.ajax({
				url:url+"/user/rank",
				method:"get",
				success:function(data){
					const data1 = data.data
					if(data.code==1){
						for(let i=0;i<data1.list.length;i++){
							str += "<li>"+
										"<div>第"+(i+1)+"名</div>"+
										"<div>"+data1.list[i].id+"</div>"+
										"<div>"+data1.list[i].money+"</div>"+
									"</li>"
						}
						$('.rank-list').html(str)
					}
				}
			})
		}
		
	}
	//排行榜
	$('.rank').click(function(){
		loadYesterdayRank(1); //请求昨日排行榜
		$('.rank-bg').show();

	})
	$('.close').click(function(){
		$('.rank-bg').hide();
	})

	//昨日佣金
	$('.yesterday').click(function(){
		$(this).find('img').attr('src','img/yesterday1.png');
		$('.total').find('img').attr('src','img/total2.png');
		loadYesterdayRank(1); //请求昨日排行榜
	})

	//总排行
	$('.total').click(function(){
		$(this).find('img').attr('src','img/total1.png');
		$('.yesterday').find('img').attr('src','img/yesterday2.png');
		loadYesterdayRank(2); //请求昨日排行榜
	})


	//点击夺宝
	$('.baby').click(function(){
		window.location.href="/static/baby.html"
	})

	$('.fish').click(function(){
		window.location.href="/static/fish.html"
	})

	//底部切换
	$('.list li').click(function(){
		let _index = $(this).index()
		if(_index != 4){
			$('.list li').find('.active2').removeClass('active')
			$('.list li').find('.no-active').removeClass('no-active1')
			$(this).find('.active2').addClass('active')
			$(this).find('.no-active').addClass('no-active1')
		}

		if(_index==0){
			$('.spread').hide();
			$('.pay').hide();
			$('.service').hide();
			$('.home').show();
			$('.money-info').hide();
		}
		if(_index==1){
			$('.spread').hide();
			$('.pay').hide();
			$('.service').hide();
			$('.home').hide();
			$('.money-info').show();
		}

		if(_index==2){
			$('.spread').show();
			$('.home').hide();
			$('.pay').hide();
			$('.service').hide();
			$('.money-info').hide();
		}

		if(_index==3){
			$('.spread').hide();
			$('.home').hide();
			$('.pay').hide();
			$('.service').show();
			$('.money-info').hide();
		}

		if(_index==4){
			$('.spread').hide();
			$(".home").hide();
			$('.service').hide();
			$(".pay").show();
			$('.money-info').hide();
		}

	})

	//充值数字按钮
	$('.pay-list li').click(function(){
		$('.pay-list li').css('background','url(./img/btn_pay2.png) no-repeat').css('background-size','100% 100%');
		$(this).css('background','url(./img/btn_pay1.png) no-repeat').css('background-size','100% 100%');
		console.log($(this).text())
	})

	//支付关闭
	$('.pay-close').click(function(){
		$('.pay').hide();
		$('.home').show();
		$('.list li').find('.active2').removeClass('active')
		$('.list li').find('.no-active').removeClass('no-active1')
		$('.list li').eq(0).find('.active2').addClass('active')
		$('.list li').eq(0).find('.no-active').addClass('no-active1')
	})

	//用户协议拒绝按钮
	$('.agree').click(function(){
		$('.protocol').hide();
	})

	
	//我的战队按钮
	$('.team-btn').click(function(){
		$('.my-team').show();
	})

	//我的战队关闭按钮
	$('.team-close').click(function(){
		$('.my-team').hide();
	})

	//推广方法介绍按钮
	$('.spread-btn').click(function(){
		$('.spread-act').show();
	})

	$('.spread-close').click(function(){
		$('.spread-act').hide();
	})

	//佣金奖励
	$('.spread-pic1').click(function(){
		$('.reward').show();
	})

	$('.reward-close').click(function(){
		$('.reward').hide();
	})


	//金币提现
	$('.gold-tixian').click(function(){
		let jinbi_money =  $('.jinbi-money').text(); //金币余额
		let today_money = $('.today-money').text(); //今日流水
		let input_value = $('.tixian-input').val(); //输入框的金额
		if(jinbi_money<1){
			$('.prompt-message').text('金币余额不足');
			$('.tixian-prompt').show();
			return
		}else if( Number(input_value) > Number(today_money)){
			$('.prompt-message').text('提现额度不能大于今日流水');
			$('.tixian-prompt').show();
			return 
		}else if(Number(input_value) < 1){
			$('.prompt-message').text('提现金额必须大于1元');
			$('.tixian-prompt').show();
			return
		}else{
			$.ajax({
				url:"gold_withdraw_url",
				method:"post",
				data:{
					money:jinbi_money,
					turnover:today_money,
					cash:input_value,
				},
				success:function(data){
					$('.pay-num').text(data.money) //首页金币余额显示
					$('.jinbi-money').text(data.money) //提现金额余额显示
				}
			})
		}
	})

	//佣金体现
	$('.yongjin-tixian-btn').click(function(){
		let yongjin_money = $('.yongjin-money').text(); //佣金余额
		let input_value2 = $('.yongjin-input').val(); //输入框的金额
		if(yongjin_money<1){
			$('.prompt-message').text('佣金余额不足');
			$('.tixian-prompt').show();
			return
		}else if(Number(input_value) < 1){
			$('.prompt-message').text('提现金额必须大于1元');
			$('.tixian-prompt').show();
			return
		}else{
			$.ajax({
				url:"brokerage_withdraw_url",
				method:"post",
				data:{
					money:yongjin_money,
					cash: input_value2,
				},
				success:function(data){
					$('.yongjin-num').text(data.money) //首页佣金显示余额
					$('.yongjin-money').text(data.money) //提现页面显示佣金余额
				}
			})
		}
	})

	$('.prompt-commit').click(function(){
		$('.tixian-prompt').hide();
	})
	
	//显示实际到账金额（金币）
	$('.tixian-input').blur(function(){
		const input_value = $('.tixian-input').val()
		$('.fast-money').text(input_value)
	})
	//显示实际到账金额（佣金）
	$('.yongjin-input').blur(function(){
		const input_value2 = $('.yongjin-input').val()
		$('.yongjin-fast').text(input_value2)
	})

	//提现按钮
	$(".tixian-btn").click(function(){
		loadWithdrawCash();
		$('.spread').hide();
		$('.home').hide();
		$('.pay').hide();
		$('.service').hide();
		$('.tixian').show();
		$('.footer').hide();
		$('.money-info').hide();
	})

	//金币提现
	$('.jinbi').click(function(){
		tixian_type = 1;
		loadWithdrawCash();
		$(this).find('img').attr('src','./img/jinbi2.png');
		$('.yongjin').find('img').attr('src','./img/yongjin1.png');
		$('.yongjin-tixian').hide();
		$('.jinbi-tixian').show();
		$.ajax({
			url:""
		})
	})

	//佣金提现
	$('.yongjin').click(function(){
		tixian_type = 2;
		loadWithdrawCash();
		$(this).find('img').attr('src','./img/yongjin2.png');
		$('.jinbi').find('img').attr('src','./img/jinbi1.png');
		$('.yongjin-tixian').show();
		$('.jinbi-tixian').hide();
		
	})


	//提现返回主页
	$('.tixian-back').click(function(){
		$('.tixian').hide();
		$('.home').show();
		$('.footer').show();
		$.ajax({
			url:"",
			data:{},
			method:"post",
			success:function(){
				
			}
		})
	})

	//资金明细导航
	$('.money-info-list li').click(function(){
		let li_index = $(this).index()
		if(li_index==0){
			$('.money-info-com').hide();
			$('.award-info-list').show();
			$('.money-info-top').css('background','url(./img/game_record.png) no-repeat').css('background-size','100% 100%');
		}

		if(li_index==1){
			$('.money-info-com').hide();
			$('.pay-record').show();
			$('.money-info-top').css('background','url(./img/pay_record.png) no-repeat').css('background-size','100% 100%');
		}

		if(li_index==2){
			$('.money-info-com').hide();
			$('.tixian-record').show();
			$('.money-info-top').css('background','url(./img/tixain_record.png) no-repeat').css('background-size','100% 100%');
		}

		if(li_index==3){
			$('.money-info-com').hide();
			$('.yongjin-record').show();
			$('.money-info-top').css('background','url(./img/yongjin_record.png) no-repeat').css('background-size','100% 100%');
		}

	})



})