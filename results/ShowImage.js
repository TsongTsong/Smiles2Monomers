function ShowImage(page, tag, size)
{
    var i = 1;
    var el;
    while (el = document.getElementById(tag + i)) {
        if (i == page){
	    el.style.display = 'block';
	    document.getElementById(tag + (i + size)).style.display = 'block';
	}
            
        else{
	    el.style.display = 'none';
	    document.getElementById(tag + (i + size)).style.display = 'none';
	}
            
        i++;
    }
}