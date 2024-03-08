var Question2 = {
    Question : "What do dogs like to eat?",
    Answer1 : "Meat",
    Answer2 : "Plastic",
    Answer3 : "Chocolate",
    Answer4 : "Electricity"
}

var Question1 = {
    Question : "Which animal is best?",
    Answer1 : "Dog",
    Answer2 : "Cat",
    Answer3 : "Duck",
    Answer4 : "Snake"
}

var Question3 = {
    Question : "What is food?",
    Answer1 : "Game controller",
    Answer2 : "Door bell",
    Answer3 : "Jacket",
    Answer4 : "Sandwich"
}

let Questions = [
    Question1,
    Question2,
    Question3
]
let c_ans = [1, 1, 4]
let ans = [0,0,0]
var max = 3;

var Alerted = false;

var i = 0;

function updateQuestion(k)
{
    var QuestionI = document.getElementById("qn");
    QuestionI.innerHTML =  "Question "+ (i+1) + "/3:";

    var QuestionI = document.getElementById("question");
    QuestionI.innerHTML = Questions[k].Question;
    
    var Input1 = document.getElementById("at1");
    Input1.innerHTML = Questions[k].Answer1;
    
    var Input1 = document.getElementById("at2");
    Input1.innerHTML = Questions[k].Answer2;
    
    var Input1 = document.getElementById("at3");
    Input1.innerHTML = Questions[k].Answer3;
    
    var Input1 = document.getElementById("at4");
    Input1.innerHTML = Questions[k].Answer4;
}

function updateChoice()
{
    if(ans[i]== "1")
    {
        var Check = document.getElementById("answer1");
        Check.checked = true;
    }
    else if(ans[i]== "2")
    {
        var Check = document.getElementById("answer2");
        Check.checked = true;
    }
    else if(ans[i]== "3")
    {
        var Check = document.getElementById("answer3");
        Check.checked = true;
    }
    else if(ans[i]== "4")
    {
        var Check = document.getElementById("answer4");
        Check.checked = true;
    }
}

function reset()
{
    for(var k = 0; k < max; k++)
    {
        ans[k]=0;
    }
    // hideQuestion();
    // unhideTable();
    // setPoints();
    // unhideScore(score);
    // toggleRestart();
    openScore();
    resetPoints();
    toggleRestart();
    hideTable();
    unhideQuestion();
    i = 0;
    updateQuestion(i);
}

function getChoice()
{
    var ele = document.getElementsByName('choice');
 
    for (k = 0; k < ele.length; k++) {
                if (ele[k].checked)
                    {   
                        ans[i]=ele[k].value
                        ele[k].checked = false;
                        return true;
                    }
            }
    return false;
    
}

function Finalchoice()
{
    console.log("choicefinalcalled");
    var ele = document.getElementsByName('choice');
 
    for (k = 0; k < ele.length; k++) {
                if (ele[k].checked)
                    {   
                        ans[i]=ele[k].value
                        return true;
                    }
            }
    return false;
    
}

let update = function (evt)
{
    evt.preventDefault()
    updateQuestion(2)
}
let openAlert = function()
{
    var ele = document.getElementById('needChoice');
        ele.style.display = 'block';
        return true;
}
let closeAlert = function()
{
    var ele = document.getElementById('needChoice');
        ele.style.display = 'none';
        return false;
}
let restoreButton = function()
{
    var next = document.getElementById('next');
    var prev = document.getElementById('prev');
    if( next.style.display == 'none')
    {
        next.style.display = 'block'
    }
    if( prev.style.display == 'none')
    {
        prev.style.display = 'block'
    }
}

let go_next = function (evt)
{
    evt.preventDefault()
    var ele = document.getElementsByName('choice');
    
    if(i < 2)
    {
        var chosen = getChoice();
        if(chosen)
        {
            i = i +1;
            updateQuestion(i);
            updateChoice();
            if (Alerted)
            {
                Alerted = closeAlert();
            }
        }
        else
        { 
            Alerted = openAlert();
            //meassage to choose answer before proceeding
        }
    }
}

let go_prev = function (evt)
{
    evt.preventDefault()
    var ele = document.getElementsByName('choice');
    
    if(i > 0)
    {
        var chosen = getChoice();
        if(chosen)
        {
            i = i - 1;
            updateQuestion(i);
            updateChoice();
            if (Alerted)
            {
                Alerted = closeAlert();
            }
        }
        else
        { 
            Alerted = openAlert();
            //meassage to choose answer before proceeding
        }
    }
}

function getTextAns(iter,num)
{
    if(num == 1)
    {
        return Questions[iter].Answer1
    }
    else if(num == 2)
    {
        return Questions[iter].Answer2
    }
    else if(num == 3)
    {
        return Questions[iter].Answer3
    }
    else if(num == 4)
    {
        return Questions[iter].Answer4
    }
    else if(num == 0)
    {
        return "Unanswered";
    }
    
}

let setStats = function()
{
    Qn = 0;
    iterator = 0;

    let ver = document.getElementById("ya1");
    ver.innerHTML = getTextAns(Qn++, ans[iterator++]);

    let ver2 = document.getElementById("ya2");
    ver2.innerHTML = getTextAns(Qn++, ans[iterator++]);

    let ver3 = document.getElementById("ya3");
    ver3.innerHTML = getTextAns(Qn, ans[iterator]);
}

let Allchosen = function()
{
    console.log("allchosen")
    for (k = 0; k < 3; k++) 
    {
        if (ans[k] == 0)
        {
            return false;
        }
    }
}

let hideQuestion = function()
{ 
    var N = document.getElementById("qn");
    N.style.display = 'none';

    var All = document.getElementById("allQ");
    All.style.display = 'none';

    var QuestionI = document.getElementById("question");
    QuestionI.style.display = 'none';

    var Submit = document.getElementById("submit");
    Submit.style.display = 'none' ;
    var Submit = document.getElementById("prev");
    Submit.style.display = 'none' ;
    var Submit = document.getElementById("next");
    Submit.style.display = 'none' ;
}

let unhideQuestion = function()
{
    var N = document.getElementById("qn");
    N.style.display = 'block';

    var All = document.getElementById("allQ");
    All.style.display = 'block';

    var QuestionI = document.getElementById("question");
    QuestionI.style.display = 'block';

    var Submit = document.getElementById("submit");
    Submit.style.display = 'block' ;
    var Submit = document.getElementById("prev");
    Submit.style.display = 'block' ;
    var Submit = document.getElementById("next");
    Submit.style.display = 'block' ;
}

let unhideTable = function()
{
    var table = document.getElementById("t");
    table.style.display = 'block';

    var table = document.getElementById("infotable");
    table.style.display = 'block';
    var table = document.getElementById("t2");
    table.style.display = 'block';
    var table = document.getElementById("t3");
    table.style.display = 'block';
    var table = document.getElementById("t4");
    table.style.display = 'block';
}

let hideTable = function()
{
    var table = document.getElementById("t");
    table.style.display = 'none';

    var table = document.getElementById("infotable");
    table.style.display = 'none';
    var table = document.getElementById("t2");
    table.style.display = 'none';
    var table = document.getElementById("t3");
    table.style.display = 'none';
    var table = document.getElementById("t4");
    table.style.display = 'none';
}


let setPoints = function()
{
    if (ans[0] == c_ans[0])
    {
        var p = document.getElementById("points1");
        p.innerHTML = 1;
    }
    if (ans[1] == c_ans[1])
    {
        var p = document.getElementById("points2");
        p.innerHTML = 1;
    }
    if (ans[2] == c_ans[2])
    {
        var p = document.getElementById("points3");
        p.innerHTML = 1;
    }
}
let resetPoints = function()
{
        var p = document.getElementById("points1");
        p.innerHTML = 0;

        var p = document.getElementById("points2");
        p.innerHTML = 0;
 
        var p = document.getElementById("points3");
        p.innerHTML = 0;
    
}

let unhideScore = function(score)
{
    var All = document.getElementById("finalscore");
    All.innerHTML = "Final Score: " + score + "/3";
    All.style.display = 'block';
}
let openScore = function()
{
    var All = document.getElementById("finalscore");
    All.style.display = 'none';
}
let toggleRestart = function()
{
    var R = document.getElementById("Restart");
    if(R.style.display == 'block')
    {
        R.style.display = 'none';
    }
    else
    {
        R.style.display = 'block'
    }
}

let final = function(evt)
{
    evt.preventDefault()
    //we need to have disabled table with score
    

    var chosen = getChoice();

    
    //make sure all choices are chosen
       
            setStats();
            //make it into getscore funciton
            let score = 0;
            for( var j = 0; j < max; j+= 1)
            {
                if ( ans[j] == c_ans[j])
                {
                    score += 1;
                }
               
            }
            if (Alerted)
            {
                Alerted = closeAlert();
            }
            hideQuestion();
            unhideTable();
            setPoints();
            unhideScore(score);
            toggleRestart();
            highlightTable();

}


document.addEventListener('DOMContentLoaded', (event) => {
    const form = document.querySelector("form");
    if (form) {
        form.addEventListener("submit", final);
    } else {
        console.error("Form not found");
    }
});

 document.addEventListener('DOMContentLoaded', (event) => {
    const form = document.querySelector("#next");
    if (form) {
        form.addEventListener("click", go_next);
    } else {
        console.error("Form not found");
    }
});

document.addEventListener('DOMContentLoaded', (event) => {
    const form = document.querySelector("#prev");
    if (form) {
        form.addEventListener("click", go_prev);
    } else {
        console.error("Form not found");
    }
});

document.addEventListener('DOMContentLoaded', (event) => {
    const form = document.querySelector("#Restart");
    if (form) {
        form.addEventListener("click", reset);
    } else {
        console.error("Form not found");
    }
});