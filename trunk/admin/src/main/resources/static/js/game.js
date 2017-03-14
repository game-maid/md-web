/**
 * 简单封装bootsrap-table中ajax设置
 */
var gameBootstrapTable = function(url, params) {
	gameAjax({
		url : url,
		data : params.data,
		callback : function(obj) {
			params.success({
				total : obj.totalElements,
				rows : obj.content
			});
		}
	});
	 if (typeof(params.complete) == "function") {
		 params.complete();
	 }
};

/**
 * option:{ 
 *   url:请求地址 
 *   type:请求方法，默认post方法 
 *   data:请求参数 
 *   async:是否同步请求，默认异步
 *   callback:回调函数 
 * }
 */
var gameAjax = function(option) {
	if (!option.url) {
		swal("", "Request url is required", "error");
		return;
	}
	if (option.type === undefined || option.type === null) {
		option.type = "post";
	}
	if (option.async === undefined || option.async === null) {
		option.async = true;
	}
	$.ajax({
		url : option.url,
		dataType : "json",
		type : option.type,
		data : option.data,
		async : option.async,
		success : function(data) {
			if (data.code == 0) {
				option.callback(data.body, data.message);
			} else {
				swal("", data.message, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			swal("Server is fucked！", errorThrown, "error");
		}
	});
}

/**
 * 简单的ajax请求，返回未封装的简单json数据(如：map)
 * option:{ 
 *   url:请求地址 
 *   type:请求方法，默认post方法 
 *   data:请求参数 
 *   async:是否同步请求，默认异步
 *   callback:回调函数 
 * }
 */
var simpleGameAjax = function(option) {
	if (!option.url) {
		swal("", "Request url is required", "error");
		return;
	}
	if (option.type === undefined || option.type === null) {
		option.type = "post";
	}
	if (option.async === undefined || option.async === null) {
		option.async = true;
	}
	$.ajax({
		url : option.url,
		dataType : "json",
		type : option.type,
		data : option.data,
		async : option.async,
		success : function(data) {
			option.callback(data);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			swal("Server is fucked！", errorThrown, "error");
		}
	});
}

var loadZoneList = function(id){
	gameAjax({
		url : "/gamezone/list",
		async : false,
		callback : function(data) {
			options = "";
			$.each(data, function(n, zone) {
				options += '<option value="'+zone.id+'">' + zone.name + '</option>';
			});
			$("#" + id).html(options);
		}
	})
}

var loadZoneMap = function(){
	var map =  {};
	gameAjax({
		url : "/gamezone/list",
		async : false,
		callback : function(data) {
			$.each(data, function(n, zone) {
				map[zone.id] = zone.name;
			});
		}
	})
	return map;
}
var loadZonePickList = function(){
	var list =  [];
	gameAjax({
		url : "/gamezone/list",
		async : false,
		callback : function(data) {
			$.each(data, function(n, zone) {
				var map={};
				map["id"] = zone.id;
				map["text"] = zone.name;
				list[n]=map;
			});
		}
	})
	return list;
}

var loadPackageList = function(id,all){
	gameAjax({
		url : "/package/list",
		callback : function(data) {
			options = "";
			if(all){
				options += "<option value=''></option>";
			}
			$.each(data, function(n, pkg) {
				options += '<option value="'+pkg.id+'">' + pkg.name + '</option>';
			});
			$("#" + id).html(options);
		}
	})
}

var loadPackageMap = function(){
	var map = {};
	gameAjax({
		url : "/package/list",
		async : false,
		callback : function(data) {
			$.each(data, function(n, pkg) {
				map[pkg.id] = pkg.name;
			});
		}
	})
	return map;
}

var loadPlatformMap = function(){
	var map = {};
	gameAjax({
		url : "/platform/list",
		async : false,
		callback : function(data) {
			$.each(data, function(n, platform) {
				map[platform.id] = platform.name;
			});
		}
	})
	return map;
}

var loadPhysicalMap = function(){
	var map = {};
	gameAjax({
		url : "/physical/list",
		async : false,
		callback : function(data) {
			$.each(data, function(n, physical) {
				map[physical.id] = physical.name;
			});
		}
	})
	return map;
}

Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

var initItemInput = function(input){
	gameAjax({
		url : "/itemSend/item",
		type : "get",
		callback : function(data) {
			window.itemAll={};
			$.each(data,function(index,val){
				itemAll[val.value]=val.name;
			});
			input.autocompleter({
		    	source: data,
		        limit: 10,
		        highlightMatches: true,
		        callback: function (value, index, selected) {
		        	if (selected) {
		        		$('#dialogcontent').find('.auto_affix').val(selected.name);
		        		} 
		        }
		    });
		}
	})
}

var initKanBanInput = function(input){
	gameAjax({
		url : "/kanBan/init",
		type : "get",
		callback : function(data) {
			window.kanbanRequest={};
			$.each(data,function(index,val){
				window.itemAll[val.value]=val.name;
				window.kanbanRequest[val.itemId]=val.count;
				window.kanbanRequest[val.value]=val.itemId;
			});
			input.autocompleter({
		    	source: data,
		        limit: 10,
		        highlightMatches: true,
		        callback: function (value, index, selected) {
		        	if (selected) {
		        		//$('#dialogcontent').find('.auto_affix').val(selected.name);
		        		$("#requestItem").val(selected.itemId+"-"+itemAll[selected.itemId]);
		        		$("#requestAmount").val(selected.count);
		        		} 
		        }
		    });
		}
	})
}

//道具名称列表（hero、skill、item、equip）
var itemNames = function(){
	gameAjax({
		url : "/itemSend/item",
		async : false,
		type : "get",
		callback : function(data) {
			window.itemAll={};
			$.each(data,function(index,val){
				itemAll[val.value]=val.name;
			});
		}
	});
}
//根据去区服id查询区服名称
var queryZoneInfoById = function(zoneIds){
	simpleGameAjax({
		url : "/gamezone/findNamesByIdIn",
		async : false,
		type : "post",
		data : {ids:zoneIds},
		callback : function(data) {
			zoneNames = data;
		}
	});
} 
//根据玩家id查询玩家昵称
var queryLordInfoById = function(lordIds){
	simpleGameAjax({
		url : "/lordInfo/findNamesByIds",
		async : false,
		type : "get",
		data : {ids:lordIds},
		callback : function(data) {
			lordNames =data;
		}
	})
} 
//获取功能名称
var queryFunctionName=function(){
	gameAjax({
		url : "/statistics/functionName",
		async : false,
		type : "get",
		callback : function(data) {
			window.functionNameMap={};
			$.each(data,function(index,val){
				functionNameMap[val.value]=val.name;
			});
		}
	});
};
//获取道具表中的道具
var queryItems = function (input){
	gameAjax({
		url : "/statistics/items",
		type : "get",
		callback : function(data) {
			itemsMap={};
			$.each(data,function(index,val){
				itemsMap[val.value]=val.name;
			});
			input.autocompleter({
		    	source: data,
		        limit: 10,
		        highlightMatches: true,
		        callback: function (value, index, selected) {
		        	if (selected) {
		        		$('#dialogcontent').find('.auto_affix').val(selected.name);
		        		} 
		        }
		    });
		}
	})
}

