const url = "http://133.186.144.236:8100/api/";

async function doSignUp(event) {
    event.preventDefault()

    try {
        if (event.target["password1"].value != event.target["password2"].value) {
            alert("비밀번호1과 비밀번호2가 서로 다릅니다.")
            return
        }

        console.log("비밀번호 확인");

        const checkStatus = await checkId(event)

        console.log("아이디 중복 확인: " + checkStatus.result);

        if (checkStatus.result) {
            alert("아이디 중복, 다른 아이디를 사용하세요.")
            return
        }

        const response = await signUp(event)
        console.log(response);

        alert("회원가입 완료")
        return

    } catch (e) {
        alert(e)
        return
    }
}

async function checkId(event) {
    const options = {
        method : 'POST',
        headers:{
            'Content-Type':'application/json'
        }
    }

    const userId = event.target["id"].value.trim()
    const check = await fetch(url + "users/" + userId + "/exist" , options)
        .then(response => {
            if(!response.ok){
                throw new Error('signUp error');
            }
            return response.json();
        });

    return check;
}

async function signUp(event) {
    const data = {
        userId: event.target["id"].value.trim(),
        userName: event.target["name"].value.trim(),
        userPassword: event.target["password1"].value
    }

    const options = {
        method : 'POST',
        headers:{
            'Content-Type':'application/json'
        },
        body : JSON.stringify(data)
    }

    const response = await fetch(url + "users/", options)
        .then(response => {
            if(!response.ok){
                throw new Error('signUp error');
            }
            return response;
        });

    return response;
}