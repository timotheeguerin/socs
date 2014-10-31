function loginFormSubmitted(form) {
	json =  {};
	json.username = form.username.value;
	url = form.action;
	getSharedKey(json.username, function (shared_key) {
		json.password = encrypt(form.password.value, parseInt(shared_key));
		post(url, 'message=' + JSON.stringify(json), function (data) {
			if(parseInt(data) == -1 ) {
				document.getElementById("error-container").className = ''
			} else {
				location = 'home.php?username=' + json.username + '&session_id=' + data;	
			}
		});
	});
	return false;
}

//Post request to the given url with the given params(in format param1=val1&parm2=val2)
function post(url, params, callack) {
	var http = new XMLHttpRequest();
	http.open('POST', url, true);
	http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	http.onreadystatechange = function() {//Call a function when the state changes.
		if(http.readyState == 4 && http.status == 200) {
			callack(http.responseText);
		}
	}
	http.send(params);
}

function getSharedKey(username, callback) {
	post('shared_key.php', 'username=' + username, callback);
}

function encrypt(str, shift) {
	var array = str.split('');
	for(var i=0; i < array.length; i ++) {
		var c = array[i].charCodeAt(0);
		if (c >= 65 && c <=  90) {
			array[i] = String.fromCharCode(mod((c + shift - 65),26) + 65);	
		} else if (c >= 97 && c <= 122) {
			array[i] = String.fromCharCode(mod((c + shift - 97),26)	 + 97);	
		}
	}
	return array.join('')
}

function mod(i, m) {
	return (i % m + m ) % m
}