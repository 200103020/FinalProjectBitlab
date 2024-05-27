var currentTabIndex = 0;
let calendar;

// Function to show the "Previous Step" button based on the current tab index
function showPreviousStepButton() {
    var previousStepButton = document.getElementById('previousStep');
    if (currentTabIndex === 0) {
        // Hide the button on the first tab
        previousStepButton.style.display = 'none';
    } else {
        // Show the button on other tabs
        previousStepButton.style.display = 'block';
    }
}

function showTab(tabName) {
    // Remove outline from all tabs
    var allTabs = document.querySelectorAll('.tab');
    allTabs.forEach(function(tab) {
        tab.style.outline = 'none';
    });

    // Set outline for the clicked tab
    var clickedTab = document.getElementById(tabName + 'Tab');
    clickedTab.style.outline = '2px solid rgb(255, 127, 80)';

    // Hide all tab contents
    var tabContents = document.getElementsByClassName("tab-content");
    for (var i = 0; i < tabContents.length; i++) {
        tabContents[i].style.display = "none";
    }

    // Show the selected tab content
    document.getElementById(tabName + "Content").style.display = "block";

    // Check if the current tab is the last one
    var lastTab = tabOrder[tabOrder.length - 1];
    if (tabName === lastTab) {
        // If it's the last tab, show the singleContent
        document.getElementById("singleContent").style.display = "block";

        var continueTabButton = document.getElementById('continueTab');
        var submitTabButton = document.getElementById('submitTabButton');

        // Hide the "Next" button and show the "Submit" button
        if (continueTabButton && submitTabButton) {
            continueTabButton.style.display = 'none';
            submitTabButton.style.display = 'block';
        }

        // Show singleContent only if the single radio button is chosen
        if (document.getElementById('singleRadio').checked) {
            document.getElementById("singleContent").style.display = "block";
        } else {
            document.getElementById("singleContent").style.display = "none";
        }
    } else {
        // If not the last tab, reset button text to "Next"
        var continueTabButton = document.getElementById('continueTab');
        var submitTabButton = document.getElementById('submitTabButton');

        if (continueTabButton && submitTabButton) {
            continueTabButton.style.display = 'block';
            submitTabButton.style.display = 'none';
        }
    }

    // Check if the current tab is the first one
    if (tabOrder.indexOf(tabName) === 0) {
        // If it's the first tab, hide the "Previous Step" button
        document.getElementById("previousStep").style.display = "none";
    } else {
        // If it's not the first tab, show the "Previous Step" button
        document.getElementById("previousStep").style.display = "block";
    }
}



function showPreviousStep() {
    // Handle logic for the "Previous Step" button
    if (currentTabIndex > 0) {
        currentTabIndex--;
        console.log("Current tab index:", currentTabIndex);
        showTab(tabOrder[currentTabIndex]);
    }
}

function continueToNextTab() {
    // Handle logic for the "Next" button
    if (currentTabIndex < tabOrder.length - 1) {
        currentTabIndex++;
        showTab(tabOrder[currentTabIndex]);
    }
}


// Array to define the order of tabs
var tabOrder = ['basicData', 'details', 'tariffs', 'singleTeam'];

// Initially show the Basic Data tab
showTab('basicData');


function toggleOptions() {
    var positionSelect = document.getElementById('positionSelect');
    positionSelect.style.display = (positionSelect.style.display === 'none' || positionSelect.style.display === '') ? 'block' : 'none';
}

function updateSelectedPosition() {
    var positionSelect = document.getElementById('positionSelect');
    var selectedPosition = positionSelect.options[positionSelect.selectedIndex].text;

    var selectedPositionText = document.getElementById('selectedPositionText');
    selectedPositionText.textContent = '- ' + selectedPosition + ' -';

    // Hide the position select after selection
    positionSelect.style.display = 'none';
}




function toggleExpOptions() {
    var experienceSelect = document.getElementById('experienceSelect');
    experienceSelect.style.display = (experienceSelect.style.display === 'none' || experienceSelect.style.display === '') ? 'block' : 'none';
}

function updateSelectedExperience() {
    var experienceSelect = document.getElementById('experienceSelect');
    var selectedExperience = experienceSelect.options[experienceSelect.selectedIndex].text;

    var selectedExperienceText = document.getElementById('selectedExperienceText');
    selectedExperienceText.textContent = '- ' + selectedExperience + ' -';

    // Hide the experience select after selection
    experienceSelect.style.display = 'none';
}



function toggleQuaOptions() {
    var quantitySelect = document.getElementById('quantitySelect');
    quantitySelect.style.display = (quantitySelect.style.display === 'none' || quantitySelect.style.display === '') ? 'block' : 'none';
}

function updateSelectedQuantity() {
    var quantitySelect = document.getElementById('quantitySelect');
    var selectedQuantity = quantitySelect.options[quantitySelect.selectedIndex].text;

    var selectedQuantityText = document.getElementById('selectedQuantityText');
    selectedQuantityText.textContent = '- ' + selectedQuantity + ' -';

    // Hide the quantity select after selection
    quantitySelect.style.display = 'none';
}

function selectBox(box) {
    // Remove the 'selected' class from all boxes
    document.querySelectorAll('.box, .box1, .box11, .box2, .box3').forEach(function (element) {
        element.classList.remove('selected');
    });

    // Add the 'selected' class to the clicked box
    box.classList.add('selected');

    // Toggle the 'white-text' class for the text color
    document.querySelectorAll('.highlight, .highlight1, .highlight11').forEach(function (element) {
        element.classList.toggle('white-text', element.parentElement.classList.contains('selected'));
    });

    // Get the value of the selected box
    const selectedValue = box.querySelector('h5').textContent.trim().substring(1); // Get the text content of the <h5> element within the clicked box, excluding the first character

    // Update the value of the input field with the selected value
    const inputField = document.querySelector('.customAmountInput');
    inputField.valueAsNumber = parseInt(selectedValue, 10);
}