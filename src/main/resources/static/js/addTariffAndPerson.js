class Tariff {
    constructor(tariffName, tariffAmount, tariffInfo) {
        this.tariffName = tariffName;
        this.tariffAmount = tariffAmount;
        this.tariffInfo = tariffInfo;
    }
}

let tariffsList = [];


function addTariffAndPerson() {
    let tariffName = document.getElementById('tariffTitleInput').value.trim();
    let tariffAmount = document.getElementById('tariffAmount').value.trim();
    let tariffShortDescription = document.getElementById('KAKInput').value.trim();

    if (tariffName === "" || tariffAmount === "" || tariffShortDescription === "") {
        alert("Please fill in all fields before adding a tariff.");
        return;
    }

    let newTariff = new Tariff(tariffName, tariffAmount, tariffShortDescription);
    tariffsList.push(newTariff);
    displayTariffs();

    document.getElementById('tariffTitleInput').value = "";
    document.getElementById('tariffAmount').value = "";
    document.getElementById('KAKInput').value = "";
}


function displayTariffs() {
    let tariffTableBody = document.getElementById('tariffTableBody');
    tariffTableBody.innerHTML = '';

    if (tariffsList.length === 0) {
        return; // If empty, exit the function
    }

    tariffsList.forEach((tariff, index) => {
        let newRow = document.createElement('tr');
        newRow.innerHTML = `<td>${tariff.tariffAmount}</td>
                            <td>${tariff.tariffName}</td>
                            <td>${tariff.tariffInfo}</td>
                            <td><button onclick="deleteTariff(${index})">Delete</button></td>`;
        tariffTableBody.appendChild(newRow);
    });
}

function deleteTariff(index) {
    tariffsList.splice(index, 1);
    displayTariffs();
}

class Team {
    constructor(position, experience, quantity, additionalInfo) {
        this.position = position;
        this.experience = experience;
        this.quantity = quantity;
        this.additionalInfo = additionalInfo;
    }
}

let personsList = [];


function addPerson() {
    let personPosition = document.getElementById('PosInput').value.trim();
    let personExperience = document.getElementById('experienceSelect').value.trim();
    let personQuantity = document.getElementById('QuantityInput').value.trim();
    let personAdditionalInfo = document.getElementById('InfoInput').value.trim();
    const selectedTextElement = document.getElementById('selectedExperienceText');

    if (personPosition === "" || personExperience === "" || personQuantity === "" || personAdditionalInfo === "") {
        alert("Please fill in all fields before adding a person.");
        return;
    }

    let newPerson = new Team(personPosition, personExperience, personQuantity, personAdditionalInfo);
    personsList.push(newPerson);
    displayPersons();

    document.getElementById('PosInput').value = "";
    document.getElementById('experienceSelect').value = "";
    document.getElementById('QuantityInput').value = "";
    document.getElementById('InfoInput').value = "";

    selectedTextElement.textContent = '- Not selected -';

}


function displayPersons() {
    let personTableBody = document.getElementById('teamBodyTable');
    personTableBody.innerHTML = '';

    if (personsList.length === 0) {
        return; // If empty, exit the function
    }

    personsList.forEach((person, index) => {
        let newRow = document.createElement('tr');
        newRow.innerHTML = `<td>${person.position}</td>
                            <td>${person.experience}</td>
                            <td>${person.quantity}</td>
                            <td>${person.additionalInfo}</td>
                            <td><button onclick="deletePerson(${index})">Delete</button></td>`;
        personTableBody.appendChild(newRow);
    });
}

function deletePerson(index) {
    personsList.splice(index, 1);
    displayPersons();
}

$(document).ready(function () {
    $("#myForm").submit(function (event) {
        event.preventDefault();

        if (validateForm()) {
            var formData = new FormData(this); // 'this' refers to the form element
            formData.append('tariffsList', JSON.stringify(tariffsList));
            formData.append('personsList', JSON.stringify(personsList));
            $.ajax({
                url: $(this).attr("action"),
                type: 'POST',
                data: formData,
                processData: false, // Important! Prevent jQuery from processing the data
                contentType: false, // Important! Set content type to false
                success: function (response) {
                    console.log(response);
                    window.location.href = "/project/my/?success";
                    clearTableRows();
                },
                error: function (xhr, status, error) {
                    console.error(xhr.responseText);
                }
            });
        } else {
            return false;
        }
    });
});

function clearTableRows() {
    tariffsList = [];
    personsList = [];
}

