const listFileDiv = document.getElementById('listFile');
const tableListFile = document.getElementById('table-list-file');
const tableListFilePath = document.getElementById('table-list-file-temp');

let formatDate = (date) => {
    let year = "" + date.getFullYear();
    let month = "" + (date.getMonth() + 1);
    if (month.length === 1) {
        month = "0" + month;
    }
    let day = "" + date.getDate();
    if (day.length === 1) {
        day = "0" + day;
    }
    let hour = "" + date.getHours();
    if (hour.length === 1) {
        hour = "0" + hour;
    }
    let minute = "" + date.getMinutes();
    if (minute.length === 1) {
        minute = "0" + minute;
    }
    return year + "/" + month + "/" + day + " " + hour + ":" + minute;
};

let setEventClickDownload = (button) => {
    button.onclick = (event) => {
        let fileId;
        if (event.target.tagName === 'I') {
            let parent = event.target.parentNode;
            fileId = parent.getAttribute("data");
        } else {
            fileId = event.target.getAttribute("data");
        }
        let urlDownload = '/download-file/' + fileId;
        window.open(urlDownload);
    }
};

let setEventClickDelete = (button) => {
    button.onclick = (event) => {
        let fileId;
        if (event.target.tagName === 'I') {
            let parent = event.target.parentNode;
            fileId = parent.getAttribute("data");
        } else {
            fileId = event.target.getAttribute("data");
        }
        axios.request({
            method: "delete",
            url: "/" + fileId
        }).then(data => {
            if (data.status === 200) {
                updateListFile();
            } else {
                alertify.error('Delete file failed', 5);
            }
        }).catch(error => {
            alertify.error('Delete file failed', 5);
        });
    }
};

let updateListFile = () => {
    axios.request({
        method: "get",
        url: "/list-file"
    }).then(data => {
        if (data.status === 200) {
            tableListFile.innerHTML = '';
            for (let i = 0; i < data.data.length; i++) {
                let fileId = data.data[i].fileId;
                let fileName = data.data[i].fileName;
                let fileSize = data.data[i].fileSize;
                let dateUpload = data.data[i].dateUpload;

                if (fileSize < 1024) {
                    fileSize = fileSize + ' bytes';
                } else if (fileSize >= 1024 && fileSize < 1048576) {
                    fileSize = Math.round(fileSize / 1024);
                    fileSize = fileSize + ' KB';
                } else if (fileSize >= 1048576 && fileSize < 1073741824) {
                    fileSize = Math.round(fileSize / 1048576);
                    fileSize = fileSize + ' MB';
                } else {
                    fileSize = Math.round(fileSize / 1073741824);
                    fileSize = fileSize + ' GB';
                }
                dateUpload = new Date(dateUpload);
                dateUpload = formatDate(dateUpload);

                let row = document.createElement('tr');
                let col1 = document.createElement('td');
                col1.innerHTML = i + 1;
                row.appendChild(col1);
                let col2 = document.createElement('td');
                col2.innerHTML = fileName;
                row.appendChild(col2);
                let col3 = document.createElement('td');
                col3.innerHTML = fileSize;
                row.appendChild(col3);
                let col4 = document.createElement('td');
                col4.innerHTML = dateUpload;
                row.appendChild(col4);
                let col5 = document.createElement('td');
                let buttonDownload = document.createElement('button');
                buttonDownload.setAttribute('name', 'buttonDownload');
                buttonDownload.setAttribute('data', fileId);
                buttonDownload.classList.add('btn');
                buttonDownload.classList.add('btn-primary');
                buttonDownload.style.marginRight = '10px';
                let iconDownload = document.createElement('i');
                iconDownload.classList.add('fas');
                iconDownload.classList.add('fa-download');
                buttonDownload.appendChild(iconDownload);
                setEventClickDownload(buttonDownload);
                col5.appendChild(buttonDownload);
                let buttonDelete = document.createElement('button');
                setEventClickDelete(buttonDelete);
                buttonDelete.setAttribute('data', fileId);
                buttonDelete.classList.add('btn');
                buttonDelete.classList.add('btn-danger');
                let iconDelete = document.createElement('i');
                iconDelete.classList.add('fas');
                iconDelete.classList.add('fa-trash');
                buttonDelete.appendChild(iconDelete);
                col5.appendChild(buttonDelete);
                row.appendChild(col5);

                tableListFile.appendChild(row);
            }
        } else {
            alertify.error('Can not load list file', 5);
        }
    }).catch(error => {
        alertify.error('Can not load list file', 5);
    });
};

updateListFile();

let updateListFilePath = () => {
    axios.request({
        method: "get",
        url: "/list-file-path"
    }).then(data => {
        if (data.status === 200) {
            tableListFilePath.innerHTML = '';
            for (let i = 0; i < data.data.length; i++) {
                let fileId = data.data[i].fileId;
                let fileName = data.data[i].fileName;
                let fileSize = data.data[i].fileSize;
                let dateUpload = data.data[i].dateUpload;

                if (fileSize < 1024) {
                    fileSize = fileSize + ' bytes';
                } else if (fileSize >= 1024 && fileSize < 1048576) {
                    fileSize = Math.round(fileSize / 1024);
                    fileSize = fileSize + ' KB';
                } else if (fileSize >= 1048576 && fileSize < 1073741824) {
                    fileSize = Math.round(fileSize / 1048576);
                    fileSize = fileSize + ' MB';
                } else {
                    fileSize = Math.round(fileSize / 1073741824);
                    fileSize = fileSize + ' GB';
                }
                dateUpload = new Date(dateUpload);
                dateUpload = formatDate(dateUpload);

                let row = document.createElement('tr');
                let col1 = document.createElement('td');
                col1.innerHTML = i + 1;
                row.appendChild(col1);
                let col2 = document.createElement('td');
                col2.innerHTML = fileName;
                row.appendChild(col2);
                let col3 = document.createElement('td');
                col3.innerHTML = fileSize;
                row.appendChild(col3);
                let col4 = document.createElement('td');
                col4.innerHTML = dateUpload;
                row.appendChild(col4);
                let col5 = document.createElement('td');
                let buttonDownload = document.createElement('button');
                buttonDownload.setAttribute('name', 'buttonDownload');
                buttonDownload.setAttribute('data', fileId);
                buttonDownload.classList.add('btn');
                buttonDownload.classList.add('btn-primary');
                buttonDownload.style.marginRight = '10px';
                let iconDownload = document.createElement('i');
                iconDownload.classList.add('fas');
                iconDownload.classList.add('fa-download');
                buttonDownload.appendChild(iconDownload);
                setEventClickDownload(buttonDownload);
                col5.appendChild(buttonDownload);
                let buttonDelete = document.createElement('button');
                setEventClickDelete(buttonDelete);
                buttonDelete.setAttribute('data', fileId);
                buttonDelete.classList.add('btn');
                buttonDelete.classList.add('btn-danger');
                let iconDelete = document.createElement('i');
                iconDelete.classList.add('fas');
                iconDelete.classList.add('fa-trash');
                buttonDelete.appendChild(iconDelete);
                col5.appendChild(buttonDelete);
                row.appendChild(col5);

                tableListFilePath.appendChild(row);
            }
        } else {
            alertify.error('Can not load list file temp', 5);
        }
    }).catch(error => {
        alertify.error('Can not load list file temp', 5);
    });
};

updateListFilePath();

let listFile = [];
document.getElementById('inputGroupFile').onchange = (event) => {
    document.getElementById('submit-button').disabled = false;
    document.getElementById('reset-button').disabled = false;
    listFileDiv.innerHTML = '';
    listFile = event.target.files;
    document.getElementById('labelFileName').innerHTML = (listFile.length === 1)
        ? (listFile[0].name) : (listFile.length + ' files');
    if (listFile.length < 10) {
        listFileDiv.style.display = 'grid';
        listFileDiv.style.gridColumnGap = '10px';
        listFileDiv.style.gridTemplateColumns = 'auto';
    } else {
        listFileDiv.style.display = 'grid';
        listFileDiv.style.gridColumnGap = '10px';
        listFileDiv.style.gridTemplateColumns = 'auto auto';
    }
    for (let i = 0; i < listFile.length; i++) {
        let container = document.createElement('div');
        container.setAttribute('data', i);
        container.setAttribute('name', 'container-upload');
        container.setAttribute('status', 'not-done');

        let fileNameDiv = document.createElement('div');
        fileNameDiv.innerHTML = (i + 1) + ', ' + listFile[i].name + ' ';
        let iconSpanDone = document.createElement('i');
        iconSpanDone.classList.add('far');
        iconSpanDone.classList.add('fa-check-circle');
        iconSpanDone.classList.add('done');
        iconSpanDone.style.color = 'green';
        iconSpanDone.hidden = true;
        fileNameDiv.appendChild(iconSpanDone);
        let iconSpanError = document.createElement('i');
        iconSpanError.classList.add('fas');
        iconSpanError.classList.add('fa-exclamation-circle');
        iconSpanError.classList.add('error');
        iconSpanError.style.color = 'red';
        iconSpanError.hidden = true;
        fileNameDiv.appendChild(iconSpanError);
        container.appendChild(fileNameDiv);

        let progress = document.createElement('div');
        progress.classList.add('progress');
        let progressBar = document.createElement('div');
        progressBar.innerHTML = '0%';
        progressBar.style.width = '0%';
        progressBar.classList.add('progress-bar');
        progressBar.classList.add('progress-bar-striped');
        progressBar.classList.add('progress-bar-animated');
        progress.appendChild(progressBar);
        container.appendChild(progress);

        let brTag = document.createElement('br');
        container.appendChild(brTag);

        listFileDiv.appendChild(container);
    }
};

let sendFile = (file, index) => {
    let formData = new FormData();
    formData.append('file', file);
    axios.request({
        method: "post",
        url: "/",
        data: formData,
        onUploadProgress: (p) => {
            let progressValue = Math.ceil((p.loaded / p.total) * 100);
            document.querySelector('div[data="' + index + '"] .progress>div').style.width = progressValue + '%';
            document.querySelector('div[data="' + index + '"] .progress>div').innerHTML = progressValue + '%';
        }
    }).then(data => {
        let progressValue = 100;
        if (data.status === 201) {
            document.querySelector('div[data="' + index + '"] .done').hidden = false;
            document.querySelector('div[data="' + index + '"] .progress>div').hidden = true;
            document.getElementById('submit-button').disabled = false;
            document.getElementById('reset-button').disabled = false;
            document.querySelector('div[data="' + index + '"]').setAttribute('status', 'done');
        } else {
            document.getElementById('reset-button').disabled = false;
            document.querySelector('div[data="' + index + '"] .error').hidden = false;
            document.querySelector('div[data="' + index + '"] .progress>div').hidden = true;
            document.querySelector('div[data="' + index + '"]').setAttribute('status', 'error');
        }
        let processBar = document.querySelectorAll('div[name="container-upload"]');
        let isDone = true;
        for (let div of processBar) {
            if (div.getAttribute('status') === 'not-done') {
                isDone = false;
            }
        }
        if (isDone) {
            let isError = false;
            for (let div of processBar) {
                if (div.getAttribute('status') === 'error') {
                    isError = true;
                }
            }
            if (!isError) {
                updateListFile();
                updateListFilePath();
                alertify.alert("Information", "Upload successfully");
                alertify.success('Upload successfully', 5);
            } else {
                alertify.error('Upload failed', 5);
            }
        }
    }).catch(error => {
        document.getElementById('reset-button').disabled = false;
        document.querySelector('div[data="' + index + '"] .error').hidden = false;
        document.querySelector('div[data="' + index + '"] .progress>div').hidden = true;
        document.querySelector('div[data="' + index + '"]').setAttribute('status', 'error');
        let processBar = document.querySelectorAll('div[name="container-upload"]');
        let isDone = true;
        for (let div of processBar) {
            if (div.getAttribute('status') === 'not-done') {
                isDone = false;
            }
        }
        if (isDone) {
            let isError = false;
            for (let div of processBar) {
                if (div.getAttribute('status') === 'error') {
                    isError = true;
                }
            }
            if (!isError) {
                updateListFile();
                updateListFilePath();
                alertify.alert("Information", "Upload successfully");
                alertify.success('Upload successfully', 5);
            } else {
                alertify.error('Upload failed', 5);
            }
        }
    });
};

document.getElementById('submit-button').onclick = (event) => {
    if (listFile.length === 0) return;
    event.target.disabled = true;
    document.getElementById('reset-button').disabled = true;
    for (let i = 0; i < listFile.length; i++) {
        sendFile(listFile[i], i);
    }
};

document.getElementById('reset-button').onclick = (event) => {
    event.target.disabled = true;
    document.getElementById('submit-button').disabled = false;
    document.getElementById('inputGroupFile').value = '';
    listFile = [];
    document.getElementById('labelFileName').innerHTML = 'Choose file';
    listFileDiv.innerHTML = '';
};

document.getElementById('deleteAll').onclick = (event) => {
    axios.request({
        method: "delete",
        url: "/"
    }).then(data => {
        if (data.status === 200) {
            updateListFile();
            updateListFilePath();
            alertify.success('Delete all file successfully', 5);
            updateListFile();
        } else {
            alertify.error('Delete all file failed', 5);
        }
    }).catch(error => {
        alertify.error('Delete all file failed', 5);
    });
};

document.getElementById('deleteAllTemp').onclick = (event) => {
    axios.request({
        method: "delete",
        url: "/temp"
    }).then(data => {
        if (data.status === 200) {
            updateListFile();
            updateListFilePath();
            alertify.success('Delete all file successfully', 5);
        } else {
            alertify.error('Delete all file failed', 5);
        }
    }).catch(error => {
        alertify.error('Delete all file failed', 5);
    });
};

document.getElementById('downloadAll').onclick = (event) => {
    let listButton = document.querySelectorAll('button[name="buttonDownload"]');
    for (let button of listButton) {
        button.click();
    }
};

let stompClient;

//Connect Websocket
let onConnected = () => {
    stompClient.subscribe('/topic/command', onMessageReceived);
};


let onError = (error) => {
    console.error(error);
};


let onMessageReceived = (payload) => {
    let message = JSON.parse(payload.body);
    if (message.command === 'BEGIN_JOIN') {
        alertify.success('File "' + message.content + '" have been joining', 7);
    }
    if (message.command === 'NEW_FILE') {
        alertify.success('File "' + message.content + '" have been joined successfully', 7);
        updateListFile();
        updateListFilePath();
    }
};

let connectWebSocket = () => {
    let socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
};

connectWebSocket();