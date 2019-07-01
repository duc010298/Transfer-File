const listFileDiv = document.getElementById('listFile');
const tableListFile = document.getElementById('table-list-file');

let notify = (title, message, alert) => {
    if (alert) {
        document.querySelector('#notify-modal .modal-header').style.backgroundColor = '#FF323F';
        document.querySelector('#notify-modal .modal-body').style.backgroundColor = '#FF323F';
        document.querySelector('#notify-modal .modal-footer').style.backgroundColor = '#FF323F';
        document.getElementById('notify-modal').style.color = '#fff';
    } else {
        document.querySelector('#notify-modal .modal-header').style.backgroundColor = '';
        document.querySelector('#notify-modal .modal-body').style.backgroundColor = '';
        document.querySelector('#notify-modal .modal-footer').style.backgroundColor = '';
        document.getElementById('notify-modal').style.color = '#000';
    }
    document.querySelector('#notify-modal .modal-title').innerHTML = title;
    document.querySelector('#notify-modal .modal-body').innerHTML = message;
    document.getElementById('notify-modal').classList.add('show');
    document.getElementById('notify-modal').style.display = 'block';
    document.getElementById('notify-modal').style.overflowX = 'hidden';
    document.getElementById('notify-modal').style.overflowY = 'auto';
    document.getElementById('notify-modal').style.backgroundColor = 'rgb(0,0,0,0.5)';
    updateListFile();
}

document.querySelector('#notify-modal .modal-footer button').onclick = (event) => {
    document.getElementById('notify-modal').classList.remove('show');
    document.getElementById('notify-modal').style.display = 'none';
}

let formatDate = (date) => {
    year = "" + date.getFullYear();
    month = "" + (date.getMonth() + 1); if (month.length == 1) { month = "0" + month; }
    day = "" + date.getDate(); if (day.length == 1) { day = "0" + day; }
    hour = "" + date.getHours(); if (hour.length == 1) { hour = "0" + hour; }
    minute = "" + date.getMinutes(); if (minute.length == 1) { minute = "0" + minute; }
    return year + "/" + month + "/" + day + " " + hour + ":" + minute;
}

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
}

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
                notify("Error", "Delete file failed", true);
            }
        }).catch(error => {
            notify("Error", "Delete file failed", true);
        });
    }
}

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

                if (fileSize < 1000) {
                    fileSize = fileSize + ' bytes';
                } else if (fileSize >= 1000 && fileSize < 1000000) {
                    fileSize = Math.round(fileSize / 1000);
                    fileSize = fileSize + ' KB';
                } else if (fileSize >= 1000000 && fileSize < 1000000000) {
                    fileSize = Math.round(fileSize / 1000000);
                    fileSize = fileSize + ' MB';
                } else {
                    fileSize = Math.round(ileSize / 1000000000);
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
            notify('Error', 'Can not load list file', true);
        }
    }).catch(error => {
        notify('Error', 'Can not load list file', true);
    });
}

updateListFile();

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
}

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
                notify('Information', 'Upload successfully', false);
            } else {
                notify('Error', 'Upload failed', true);
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
                notify('Information', 'Upload successfully', false);
            } else {
                notify('Error', 'Upload failed', true);
            }
        }
    });
}

document.getElementById('submit-button').onclick = (event) => {
    if (listFile.length === 0) return;
    event.target.disabled = true;
    document.getElementById('reset-button').disabled = true;
    for (let i = 0; i < listFile.length; i++) {
        sendFile(listFile[i], i);
    }
}

document.getElementById('reset-button').onclick = (event) => {
    event.target.disabled = true;
    document.getElementById('submit-button').disabled = false;
    document.getElementById('inputGroupFile').value = '';
    listFile = [];
    document.getElementById('labelFileName').innerHTML = 'Choose file';
    listFileDiv.innerHTML = '';
}

document.getElementById('deleteAll').onclick = (event) => {
    axios.request({
        method: "delete",
        url: "/"
    }).then(data => {
        if (data.status === 200) {
            notify("Information", "Delete all file successfully", false);
            updateListFile();
        } else {
            notify("Error", "Delete all file failed", true);
        }
    }).catch(error => {
        notify("Error", "Delete all file failed", true);
    });
}

document.getElementById('downloadAll').onclick = (event) => {
    //TODO download as zip
    let listButton = document.querySelectorAll('button[name="buttonDownload"]');
    for (let button of listButton) {
        button.click();
    }
}