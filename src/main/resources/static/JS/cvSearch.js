//Please enter your own URRL
var url = "http://localhost:8080/CVs";

//Getting the list of files
$(document).ready(function() {
	let all_url = url+"/getAll";
	$.ajax({ 
	    type: "GET",
	    processData: false,
	    contentType: false,
	    url: all_url, 
	    success: function( data ) {
	    	var ul = document.getElementById('allFiles');

	    	//Get files list
			while (ul.firstChild) {
				ul.removeChild(ul.lastChild);
			}
					
	    	for(var k in data) {
	    		var li = document.createElement('li');
		    	li.textContent = data[k];
		    	li.setAttribute('class', 'list-group-item d-flex justify-content-between align-items-center');
		    	ul.appendChild(li);
		    	
		    	var a = document.createElement('a');
		    	a.textContent = "delete";
		    	a.setAttribute('href', '#');
		    	a.setAttribute('onclick', 'remove_file("'+data[k]+'")');
		    	li.appendChild(a);
	    	}
	    },
		error : function(err) {
			alert("Error when loading files from server");
		}
	  });  
});

$(".custom-file-input").on("change", function() {
	  var fileName = $(this).val().split("\\").pop();
	  $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
});

//To upload file
function upload_file() {
	if (!window.File || !window.FileReader || !window.FileList || !window.Blob) {
		alert('The File APIs are not fully supported in this browser.');
		return;
	}

	var input = document.getElementById('inputFile');
	
	if (!input) {
		alert("Couldn't find the fileinput element.");
	} else if (!input.files) {
		alert("This browser doesn't seem to support the `files` property of file inputs.");
	} else if (!input.files[0]) {
		alert("Please select a file before clicking 'Send'");
	} else {
		var file = input.files[0];
		var fr = new FileReader();
		var formdata = new FormData();
		formdata.append("file",file);

		let add_url = url+"/add";

		$.ajax({ 
		    type: "POST",
		    processData: false,
		    contentType: false,
		    url: add_url, 
		    data: formdata, 
		    success: function( data ) {
		    	
		    	//Get files list
		    	var ul = document.getElementById('allFiles');

				while (ul.firstChild) {
					ul.removeChild(ul.lastChild);
				}
						
		    	for(var k in data) {
		    		var li = document.createElement('li');
			    	li.textContent = data[k];
			    	li.setAttribute('class', 'list-group-item d-flex justify-content-between align-items-center');
			    	ul.appendChild(li);
			    	
			    	var a = document.createElement('a');
			    	a.textContent = "delete";
			    	a.setAttribute('href', '#');
			    	a.setAttribute('onclick', 'remove_file("'+data[k]+'")');
			    	li.appendChild(a);

		    	}
		    	//Refresh input file
		    	var div = document.getElementById("labelFile");
		    	div.value = null;
		    	div.remove();
		    	var inp = document.getElementById("inputFile");
				var div = document.getElementById("divFile");
				
				var label = document.createElement('label');
				label.setAttribute('class', 'custom-file-label');
				label.setAttribute('id', 'labelFile');
				label.setAttribute('for', 'inputFile');
				label.setAttribute('placeholder', 'Browse and choose file');
				label.textContent = "Browse and choose file";
		    	
				div.insertBefore(label, inp);
				
				input.value = null;
		    }, 
			error : function(err) {
				alert("Error when loading files from server");
			}
		  });  
	}
}

//To search for keywords
function search() {
	var text = $("#searchText").val();
	if((text != "") && (text != null)){
		let search_url = url+"/search/"+text;
		
		$.ajax({ 
		    type: "GET",
		    processData: false,
		    contentType: false,
		    url: search_url, 
		    success: function( data ) { 
	
		    	var ul = document.getElementById('searchRes');
		    	
				while (ul.firstChild) {
					ul.removeChild(ul.lastChild);
				}
		    	  
		    	for(var k in data) {
		    		var li = document.createElement('li');
			    	li.textContent = data[k];
			    	li.setAttribute('class', 'list-group-item');
			    	ul.appendChild(li);
			    	
		    	}
		    }, 
			error : function(err) {
				alert("Error when loading files from server");
			}
		  });  	
		//Reset keyword text input
		$("#searchText").val("");
	}else{
		alert("Please enter a keyword before clicking 'Search'");
	}
}

//To remove file
function remove_file(id) {
	let rem_url = url+"/remove/"+id;

	$.ajax({ 
	    type: "DELETE",
	    processData: false,
	    contentType: false,
	    url: rem_url, 
	    success: function( data ) { 
	    	//Get files list
	    	var ul = document.getElementById('allFiles');

			while (ul.firstChild) {
				ul.removeChild(ul.lastChild);
			}
					
	    	for(var k in data) {
	    		var li = document.createElement('li');
		    	li.textContent = data[k];
		    	li.setAttribute('class', 'list-group-item d-flex justify-content-between align-items-center');
		    	ul.appendChild(li);
		    	
		    	var a = document.createElement('a');
		    	a.textContent = "delete";
		    	a.setAttribute('href', '#');
		    	a.setAttribute('onclick', 'remove_file("'+data[k]+'")');
		    	li.appendChild(a);
	    	}

	    }, 
		error : function(err) {
			alert("Error when loading files from server");
		}
	  });  
}