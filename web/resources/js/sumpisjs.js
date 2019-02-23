/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 25th-Nov-2017
 */

$(document).ready(test);

function test()
{
    $('input.input_Field').focusout(function(event)
        {
            if(event.target===this)
            {
                if($(this).val()==='')
                {
                    $(this).removeClass('unmatched').addClass('error_Class');
                }
            }
        });
    
    $('input.input_Field').focusin(function(event)
        {
            $('input.input_FieldA').removeClass('error_ClassA');
            $('input.input_FieldSpecial').removeClass('error_Class');
            $('#error_Login').empty();
            $('#search_Info').empty();
            $('#outcome').empty(); 
            $('#adminPageInfo').empty();
            $('#error_Setup').empty();
            if(event.target===this)
            {
                $(this).removeClass('error_Class');
            }
        });
        
    /*if username field has value but the username already exists in the database*/
    $('#ajax_Username').focusout(function(event)
    {
        if(event.target===this)
        {
            if($(this).val()!=='')
            {
                $.get('ajaxUsernameCheck?sent='+ $(this).val(), function(data)
                {
                    /*
                    This is old skool now
                    if(data==='exists')
                    {
                        $('#ajax_Username').addClass('unmatched');
                        $('#ajax_Username_Info').html('Username exists pick a<br>different username.');
                    }
                    */
                    
                    var data2=JSON.parse(data);
                    if(data2.report==='yes')
                    {
                        $('#ajax_Username').addClass('unmatched');
                        $('#ajax_Username_Info').html('Username exists pick a<br>different username.');
                    }
                });
                
                /*SENDING JSON TO SERVER
                var obj={
                    response:"My name is Dexter and I am ",
                    report: "27"
                };
                
                
                $.get('ajaxUsernameCheck?sent='+ encodeURI(JSON.stringify(obj)), function(data)
                {
                    $('#ajax_Username').addClass('unmatched');
                    $('#ajax_Username_Info').html(data);
                });
                */
            }
        }
    });
    
    /*if username field has value but the username already exists in the adminTable database*/
    $('#username_Admin').focusout(function(event)
    {
        if(event.target===this)
        {
            if($(this).val()!=='')
            {
                $.get('adminUsernameCheck?sent='+ $(this).val(), function(data)
                {
                    if(data==='exists')
                    {
                        $(this).addClass('unmatched');
                        $('#admin_Username_Info').text('Username exists');
                    }
                    else
                    {
                        $('#admin_Username_Info').text('Username does not exist');
                    }
                });                
            }
        }
    });
    
    $('#username_Admin').focusin(function(event)
    {
        if(event.target===this)
        {
            $('this').removeClass('unmatched');
            $('#admin_Username_Info').text('');
        }
    });
    
    $('#ajax_Username').focusin(function(event)
        {
            $('input.input_FieldA').removeClass('error_ClassA');
            $('input.input_FieldSpecial').removeClass('error_Class');
            $('#ajax_Username').removeClass('unmatched');
            $('#ajax_Username_Info').empty();
            $('#error_Login').empty();
            $('#search_Info').empty();
            $('#outcome').empty();            
            if(event.target===this)
            {
                $(this).removeClass('error_Class');
            }
        });
        
    /*for checking the number off character in password*/
    $('#pswd_Length1').focusout(function(event)
        {
            if(event.target===this)
            {
                if(parseInt($(this).val().length)<8)
                {
                    $(this).removeClass('unmatched').addClass('error_Class');
                    $('#pswd_Info1').html('Password must be<br>8 characters or more.');
                }
                else
                {
                    $(this).removeClass('unmatched').removeClass('error_Class');
                    $('#pswd_Info1').empty();
                }
            }
        });
    
    $('#pswd_Length1').focusin(function(event)
        {
            $('input.input_FieldA').removeClass('error_ClassA');
            $('input.input_FieldSpecial').removeClass('error_Class');
            $('#pswd_Length1').removeClass('error_Class');
            $('#error_Login').empty();
            $('#search_Info').empty();
            $('#outcome').empty();            
            if(event.target===this)
            {
                $(this).removeClass('error_Class');
            }
        });
        
        $('#pswd_Length2').focusout(function(event)
        {
            if(event.target===this)
            {
                if(parseInt($(this).val().length)<8)
                {
                    $(this).removeClass('unmatched').addClass('error_Class');
                    $('#pswd_Info2').html('Password must be<br>8 characters or more.');
                }
                else
                {
                    $(this).removeClass('unmatched').removeClass('error_Class');
                    $('#pswd_Info2').empty();
                }
            }
        });
    
    $('#pswd_Length2').focusin(function(event)
        {
            $('input.input_FieldA').removeClass('error_ClassA');
            $('input.input_FieldSpecial').removeClass('error_Class');
            $('#pswd_Length2').removeClass('error_Class');
            $('#error_Login').empty();
            $('#search_Info').empty();
            $('#outcome').empty();            
            if(event.target===this)
            {
                $(this).removeClass('error_Class');
            }
        });
    
    /*for mobile number validation*/
    $('#telephone').focusout(function(event)
        {
            if(event.target===this)
            {
                
                if($(this).val().charAt(0)!=='+')
                {
                    $(this).addClass('error_Class');
                    $('#telephone_Info').html('Include country code<br>e.g +144521447').css('color', 'red');
                    event.preventDefault();
                }
                else if(!$(this).val().match(('[+][1-9][0-9]*[1-9]\\d{2}[1-9]\\d{6}')))
                {
                    $(this).addClass('error_Class');
                    $('#telephone_Info').html('Invalid mobile number.').css('color', 'red');
                    event.preventDefault();
                }
                else
                {
                    $(this).removeClass('error_Class');
                    $('#telephone_Info').empty();
                }
            }
        });
    
    $('#telephone').focusin(function(event)
        {
            $('input.input_FieldA').removeClass('error_ClassA');
            $('input.input_FieldSpecial').removeClass('error_Class');
            $('#pswd_Length1').removeClass('error_Class');
            $('#error_Login').empty();
            $('#search_Info').empty();
            $('#outcome').empty();            
            if(event.target===this)
            {
                $(this).removeClass('error_Class');
            }
        });
    
    
    
        
    /*FOR PASSWORD UPDATE*/    
    $('input.input_FieldG').focusout(function(event)
        {
            if(event.target===this)
            {
                if($(this).val()==='')
                {
                    $(this).removeClass('unmatched').addClass('error_Class');
                }
            }
        });
    
    $('input.input_FieldG').focusin(function(event)
        {
            $('input.input_FieldA').removeClass('error_ClassA');
            $('input.input_FieldSpecial').removeClass('error_Class');
            $('#error_Login').empty();
            $('#search_Info').empty();
            $('#outcome').empty();            
            if(event.target===this)
            {
                $(this).removeClass('error_Class');
            }
        });
    $('#signup_ButtonG').click(function(event)
        {
            if(event.target===this)
            {
                /*this check is for password field to check if its up to 8 characters*/
                if(parseInt($('#pswd_Length1').val().length)<8 || parseInt($('#pswd_Length1').val().length)<8)
                {
                    $('#pswd_Length1').removeClass('unmatched').addClass('error_Class');
                    $('#pswd_Length2').removeClass('unmatched').addClass('error_Class');
                    event.preventDefault();
                }
                
                if($('#admin_Username_Info').text()===('Username does not exist'))
                {
                    event.preventDefault();
                }
                
                var input_Element=$('input.input_FieldG');
                for(var x=0; x<input_Element.length; x++)
                {
                    /*checks that no field is empty*/
                    if($(input_Element[x]).val()==='')
                    {
                        $(input_Element[x]).addClass('error_Class');
                        $('#error_jquery').html('Fill field(s) in red.');
                        event.preventDefault();
                    }
                                        
                    /*if password fields are not equal and the first password field is empty*/
                    if($(input_Element[2]).val()!==$(input_Element[3]).val() && $(input_Element[2]).val()==='')
                    {
                        $(input_Element[2]).addClass('error_Class');
                        $(input_Element[3]).removeClass('error_Class').addClass('unmatched');
                        $('#error_paswd').html('Passwords did not match.');
                        event.preventDefault();
                    }
                    /*if password fields are not equal and the second password field is empty*/
                    else if($(input_Element[2]).val()!==$(input_Element[3]).val() && $(input_Element[3]).val()==='')
                    {
                        $(input_Element[2]).removeClass('error_Class').addClass('unmatched');
                        $(input_Element[3]).addClass('error_Class');
                        $('#error_paswd').html('Passwords did not match.');
                        event.preventDefault();
                    }
                    /*if password fields are not equal and the two password fields are not empty*/
                    else if($(input_Element[2]).val()!==$(input_Element[3]).val() 
                    && ($(input_Element[2]).val()!=='' && $(input_Element[3]).val()!==''))
                    {
                        $(input_Element[2]).removeClass('error_Class').addClass('unmatched');
                        $(input_Element[3]).removeClass('error_Class').addClass('unmatched');
                        $('#error_jquery').empty();
                        $('#error_paswd').html('Passwords did not match.');
                        event.preventDefault();
                    }
                    /*if password fields are equal and any password field is empty*/
                    else if($(input_Element[2]).val()===$(input_Element[3]).val() 
                    && ($(input_Element[2]).val()==='' || $(input_Element[3]).val()===''))
                    {
                        $(input_Element[2]).removeClass('unmatched').addClass('error_Class');
                        $(input_Element[3]).removeClass('unmatched').addClass('error_Class');
                        $('#error_paswd').empty();
                        event.preventDefault();
                    }
                    /* ACCEPTS THIS: if password fields are equal and the two password field are not empty*/
                    else if($(input_Element[2]).val()===$(input_Element[3]).val() 
                    && ($(input_Element[2]).val()!=='' && $(input_Element[3]).val()!==''))
                    {
                        $(input_Element[2]).removeClass('unmatched').removeClass('error_Class');
                        $(input_Element[3]).removeClass('unmatched').removeClass('error_Class');
                        $('#error_paswd').empty();
                    }
                }
                
            }
        });
    /*input_FieldSpecial is for file share description field (found inside message: for user and admin)*/    
    $('input.input_FieldSpecial').focusout(function(event)
        {
            if(event.target===this)
            {
                if($(this).val()==='')
                {
                    $(this).removeClass('unmatched').addClass('error_Class');
                }
            }
        });
    
    $('input.input_FieldSpecial').focusin(function(event)
        {
            $('input.input_Field').removeClass('error_Class');
            $('#txt_Area_InformAll').removeClass('error_Class');
            $('input.input_FieldA').removeClass('error_ClassA');
            $('#search_Info').empty();
            $('#outcome').empty();
            if(event.target===this)
            {
                $(this).removeClass('error_Class');
            }
        });
        
    /*registration page submission code*/    
    $('#signup_Button').click(function(event)
        {
            $('input.input_FieldA').removeClass('error_ClassA');
            $('#error_Login').empty();
            
            if(event.target===this)
            {
                var input_Element=$('input.input_Field');
                for(var x=0; x<input_Element.length; x++)
                {
                    /*checks that no field is empty*/
                    if($(input_Element[x]).val()==='')
                    {
                        $(input_Element[x]).addClass('error_Class');
                        $('#error_jquery').html('Fill field(s) in red.');
                        event.preventDefault();
                    }
                    /*this check is for ajax username check when button is clicked*/
                    if($('#ajax_Username').hasClass('unmatched'))
                    {
                        event.preventDefault();
                    }
                    
                    /*this check is for telephone starting without + country code*/
                    if($('#telephone').hasClass('error_Class'))
                    {
                        event.preventDefault();
                    }
                                        
                    /*this check is for password field to check if its up to 8 characters*/
                    if(parseInt($('#pswd_Length1').val().length)<8 || parseInt($('#pswd_Length1').val().length)<8)
                    {
                        $('#pswd_Length1').removeClass('unmatched').addClass('error_Class');
                        $('#pswd_Length2').removeClass('unmatched').addClass('error_Class');
                        event.preventDefault();
                    }
                    
                    /*if password fields are not equal and the first password field is empty*/
                    if($(input_Element[4]).val()!==$(input_Element[5]).val() && $(input_Element[4]).val()==='')
                    {
                        $(input_Element[4]).addClass('error_Class');
                        $(input_Element[5]).removeClass('error_Class').addClass('unmatched');
                        $('#error_paswd').html('Passwords did not match.');
                        event.preventDefault();
                    }
                    /*if password fields are not equal and the second password field is empty*/
                    else if($(input_Element[4]).val()!==$(input_Element[5]).val() && $(input_Element[5]).val()==='')
                    {
                        $(input_Element[4]).removeClass('error_Class').addClass('unmatched');
                        $(input_Element[5]).addClass('error_Class');
                        $('#error_paswd').html('Passwords did not match.');
                        event.preventDefault();
                    }
                    /*if password fields are not equal and the two password fields are not empty*/
                    else if($(input_Element[4]).val()!==$(input_Element[5]).val() 
                    && ($(input_Element[4]).val()!=='' && $(input_Element[5]).val()!==''))
                    {
                        $(input_Element[4]).removeClass('error_Class').addClass('unmatched');
                        $(input_Element[5]).removeClass('error_Class').addClass('unmatched');
                        $('#error_jquery').empty();
                        $('#error_paswd').html('Passwords did not match.');
                        event.preventDefault();
                    }
                    /*if password fields are equal and any password field is empty*/
                    else if($(input_Element[4]).val()===$(input_Element[5]).val() 
                    && ($(input_Element[4]).val()==='' || $(input_Element[4]).val()===''))
                    {
                        $(input_Element[4]).removeClass('unmatched').addClass('error_Class');
                        $(input_Element[5]).removeClass('unmatched').addClass('error_Class');
                        $('#error_paswd').empty();
                        event.preventDefault();
                    }
                    /* ACCEPTS THIS: if password fields are equal and the two password field are not empty*/
                    else if($(input_Element[4]).val()===$(input_Element[5]).val() 
                    && ($(input_Element[4]).val()!=='' && $(input_Element[4]).val()!==''))
                    {
                        $(input_Element[4]).removeClass('unmatched').removeClass('error_Class');
                        $(input_Element[5]).removeClass('unmatched').removeClass('error_Class');
                        $('#error_paswd').empty();
                    }
                }
            }
        });
        
    /*for registration.jsp and homepage.jsp login input fields as well as other fields on the green grass background*/     
    $('input.input_FieldA').focusout(function(event)
        {
            if(event.target===this)
            {
                if($(this).val()==='')
                {
                    $(this).addClass('error_ClassA');
                }
            }
        });
    
    $('input.input_FieldA').focusin(
        function(event)
        {
            $('input.input_Field').removeClass('error_Class');
            $('#txt_Area_InformAll').removeClass('error_Class');
            $('input.input_FieldSpecial').removeClass('error_Class');
            $('#txt_Area').removeClass('error_Class');
            $('#txt_Area').removeClass('error_Class');
            $('#txt_AreaB').removeClass('error_Class');
            $('#specify_Count').removeClass('error_Class');
            $('#error_jquery').empty();
            $('#error_paswd').empty();
            $('#error_Login').empty();
            $('#search_Info').empty();
            $('#outcome').empty();
            $('#app_Info').empty();
            $('#adminPageInfo').empty();
            if(event.target===this)
            {
                $(this).removeClass('error_ClassA');
            }
        });
    /*for registration.jsp and homepage.jsp login form*/    
    $('#login_Button').click(function(event)
        {
            $('input.input_Field').removeClass('error_Class');
            $('#txt_Area_InformAll').removeClass('error_Class');
            $('input.input_FieldSpecial').removeClass('error_Class');
            $('#error_jquery').empty();
            $('#error_paswd').empty();
            if(event.target===this)
            {
                var input_Element=$('input.input_FieldA');
                for(var x=0; x<input_Element.length; x++)
                {
                    /*checks that no field is empty*/
                    if($(input_Element[x]).val()==='')
                    {
                        $(input_Element[x]).addClass('error_ClassA');
                        $('#error_Login').html('Fill field(s) in yellow.');
                        event.preventDefault();
                    }
                }
                
            }
        });
    /*for accountsetup.jsp button*/    
    $('#input_SubmitB').click(
        function(event)
        {
            if(event.target===this)
            {
                var input_Element=$('input.input_Field');
                for(var x=0; x<input_Element.length; x++)
                {
                    /*checks that no field is empty*/
                    if($(input_Element[x]).val()==='')
                    {
                        $(input_Element[x]).addClass('error_Class');
                        $('#error_Setup').html('Fill field(s) in red.');
                        event.preventDefault();
                    }
                }
                
                /*this check is for telephone starting without + country code*/
                if($('#telephone').hasClass('error_Class'))
                {
                    event.preventDefault();
                }
                               
                if($('#admin_Username_Info').text()===('Username exists'))
                {
                    event.preventDefault();
                }
                
                if(parseInt($('#pswd_Length1').val().length)<8)
                {
                    $('#pswd_Length1').removeClass('unmatched').addClass('error_Class');
                    event.preventDefault();
                }                
            }
        });
    
    /*for adminInboxMessages.jsp*/
    $('#txt_Area').focusin(function(event)
    {
        if(event.target===this)
        {
            $('#txt_Area').removeClass('error_Class');
            $('#txt_AreaB').removeClass('error_Class');
            $('input.input_FieldA').removeClass('error_ClassA');
            $('#error_Inbox').empty();
            $('#search_Info').empty();
            $('#app_Info').empty();
        }
    });
    $('#txt_AreaB').focusin(function(event)
    {
        if(event.target===this)
        {
            $('#txt_Area').removeClass('error_Class');
            $('#txt_AreaB').removeClass('error_Class');
            $('input.input_FieldA').removeClass('error_ClassA');
            $('#specify_Count').removeClass('error_Class');
            $('#error_Inbox').empty();
            $('#search_Info').empty();
            $('#app_Info').empty();
        }
    });
    $('#button_A, #button_B, #button_C, #button_D, #button_E, #button_F, #button_XX').click(function(event)
        {
            if(event.target===this)
            {
                $('#txt_AreaB').removeClass('error_Class');
                $('input.input_FieldA').removeClass('error_ClassA');
                $('#search_Info').empty();
                if($('#txt_Area').val()==='')
                {
                    $('#txt_Area').addClass('error_Class');
                    //$('#error_Inbox').html('Content is empty');
                    event.preventDefault();
                }                
            }
        });
    $('#button_G, #button_H, #button_I, #button_J').click(
        function(event)
        {
            if(event.target===this)
            {
                $('#txt_Area').removeClass('error_Class');
                $('input.input_FieldA').removeClass('error_ClassA');
                $('#specify_Count').removeClass('error_Class');
                $('#search_Info').empty();
                $('#app_Info').empty();
                if($('#txt_AreaB').val()==='')
                {
                    $('#txt_AreaB').addClass('error_Class');
                    //$('#error_Inbox').html('Content is empty');
                    event.preventDefault();
                }                
            }
        });
    $('#button_C, #button_D, #button_E').click(function(event)
        {
            if(event.target===this)
            {
                $('#txt_AreaB').removeClass('error_Class');
                $('input.input_FieldA').removeClass('error_ClassA');
                $('#search_Info').empty();
                
                if($('#txt_Area').val()!=='')
                {
                    if(parseInt($('#specify_Count').val())===0 || $('#specify_Count').val()==='')
                    {
                        $('#specify_Count').addClass('error_Class');
                        $('#app_Info').html('Enter a number greater than zero.');
                        //$('#error_Inbox').html('Content is empty');
                        event.preventDefault();
                    }
                }                
            }
        });
        
    /*for adminInfoPage, even tho' the New Admin button is benefitting from refactoring*/
    $('#remove_Admin').click(function(event)
    {
        $('input.input_Field').removeClass('error_Class');
        if(event.target===this)
        {
            if($('#admin_Username_Info').text()===('Username does not exist'))
            {
                event.preventDefault();
            }
                
            if($('#username_Admin').val()==='admin')
            {
                $('#username_Admin').addClass('error_Class');
                $('#error_Setup').html('Cannot remove base Administrator');
                event.preventDefault();
            }
            else if($('#username_Admin').val()==='')
            {
                $('#username_Admin').addClass('error_Class');
                $('#error_Setup').html("Enter admin's username to remove");
                event.preventDefault();
            }
        }
    });
    $('#txt_Area_InformAll').focusout(function(event)
    {
        if(event.target===this)
        {
            if($(this).val()==='')
            {
                $(this).addClass('error_Class');
                $('#error_Setup').empty();
            }
        }
    });
    $('#txt_Area_InformAll').focusin(function(event)
    {
        $('input.input_FieldSpecial').removeClass('error_Class');
        $('input.input_FieldA').removeClass('error_ClassA');
        $('#outcome').empty();
        $('#search_Info').empty();
        $('#adminPageInfo').empty();
        if(event.target===this)
        {
            $(this).removeClass('error_Class');
            $('#error_Setup').empty();
        }
    });
    
    $('#txt_Area_InformAll2').focusout(function(event)
    {
        if(event.target===this)
        {
            if($(this).val()==='')
            {
                $(this).addClass('error_Class');
                $('#error_Setup').empty();
            }
        }
    });
    $('#txt_Area_InformAll2').focusin(function(event)
    {
        $('input.input_FieldSpecial').removeClass('error_Class');
        $('input.input_FieldA').removeClass('error_ClassA');
        $('#outcome').empty();
        $('#search_Info').empty();
        $('#adminPageInfo').empty();
        if(event.target===this)
        {
            $(this).removeClass('error_Class');
            $('#error_Setup').empty();
        }
    });
    
    $('#inform_All_Button').click(function(event)
    {
        if(event.target===this)
        {
            if($('#txt_Area_InformAll').val()==='')
            {
                $('#txt_Area_InformAll').addClass('error_Class');
                $('#error_Setup').html('Please write a message to pass.');
                event.preventDefault();
            }
        }
    });
    
    /*for posting messages*/
    $('#post_Message, #post_Message2').click(function(event)
    {
        if(event.target===this)
        {
            $('input.input_FieldSpecial').removeClass('error_Class');
            $('input.input_FieldA').removeClass('error_ClassA');
            $('#search_Info').empty();
            
            var input_Element=$('input.input_Field');
            for(var x=0; x<input_Element.length; x++)
            {
                if($(input_Element[x]).val()==='')
                {
                    $(input_Element[x]).addClass('error_Class');
                    $('#outcome').html('Enter fields in red border');
                    $('#adminPageInfo').text('Fill the red-border field(s) with the relevant data.');
                    event.preventDefault();
                }
            }
            if($('#txt_Area_InformAll').val()==='')
            {
                $('#txt_Area_InformAll').addClass('error_Class');
                $('#outcome').html('Enter field(s) in red border');
                $('#adminPageInfo').text('Fill the red-border field(s) with the relevant data.');
                event.preventDefault();
            }
            if($('#txt_Area_InformAll2').val()==='')
            {
                $('#txt_Area_InformAll2').addClass('error_Class');
                $('#outcome').html('Enter field(s) in red border');
                $('#adminPageInfo').text('Fill the red-border field(s) with the relevant data.');
                event.preventDefault();
            }
        }
    });
    
    /*for uploading file*/
    $('#upload_File').click(function(event)
    {
        if(event.target===this)
        {
            $('input.input_Field').removeClass('error_Class');
            $('#txt_Area_InformAll').removeClass('error_Class');
            $('input.input_FieldA').removeClass('error_ClassA');
            $('#search_Info').empty();
            if($('input.input_FieldSpecial').val()==='')
            {
                $('input.input_FieldSpecial').addClass('error_Class');
                $('#outcome').html('Enter field(s) in red border');
                event.preventDefault();
            }
        }
    });
    
    /*search box*/
    $('#search_Button').click(function(event)
    {
        $('input.input_Field').removeClass('error_Class');
        $('input.input_FieldSpecial').removeClass('error_Class');
        $('#txt_Area_InformAll').removeClass('error_Class');
        $('#specify_Count').removeClass('error_Class');
        $('#txt_Area').removeClass('error_Class');
        $('#txt_Area').removeClass('error_Class');
        $('#txt_AreaB').removeClass('error_Class');
        $('#outcome').empty();
        $('#app_Info').empty();
        
        if(event.target===this)
        {
            if($('input.input_FieldA').val()==='')
            {
                $('input.input_FieldA').addClass('error_ClassA');
                $('#search_Info').html('Enter the field in yellow border.');
                event.preventDefault();
            }
        }
    });
    
    //this one is for double context words in adminpage.jsp
    $('#doubleContext, #doubleContext2').click(function(event)
    {
        if(event.target===this)
        {
            $('.input_FieldA').removeClass('error_ClassA');
            $('.input_Field').removeClass('error_Class');
            if($('#txt_Area_InformAll').val()==='')
            {
                event.preventDefault();
                $('#txt_Area_InformAll').addClass('error_Class');
                $('#adminPageInfo').text('Fill the textarea with the relevant query.');
            }
        }
    });
    
    $('#deleteWord, #deleteWord2').click(function(event)
    {
        if(event.target===this)
        {
            $('.input_FieldA').removeClass('error_ClassA');
            $('#txt_Area_InformAll').removeClass('error_Class');
            
            if($('.input_Field').val()==='')
            {
                event.preventDefault();
                $('.input_Field').addClass('error_Class');
                $('#adminPageInfo').text('Enter the red-border field with the relevant data.');
            }
        }
    });
    
    //this is for sumbloggers page ban block etc button
    $('#XX1, #XX2, #XX3, #XX4').click(function(event)
    {
        if(event.target===this)
        {
            $('input.input_FieldSpecial').removeClass('error_Class');
            $('input.input_FieldA').removeClass('error_ClassA');
            $('#search_Info').empty();
            
            var input_Element=$('input.input_Field')[0];
            if($(input_Element).val()==='')
            {
                $(input_Element).addClass('error_Class');
                $('#outcome').html('Enter fields in red border');
                $('#adminPageInfo').text('Fill the circled field with the relevant data.');
                event.preventDefault();
            }
        }
    });
}