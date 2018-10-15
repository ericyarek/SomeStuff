package someShit;

public class practice {
	public static void main(String[] args) {
		
		xyzThere("xyzwoiportdi");
		
	}
		

	
	public static void xyzThere(String str) {
		  
		  
		for (int i=str.length()-3; i>=0; i--){
		  System.out.println(str.substring(i, i+3)); {
		  
		}
		}
	}
	
}


function Get-URLStatus
{
    [CmdletBinding()]

    Param
    (
        #Param1 help description
        [Parameter(Mandatory=$true,
                   ValueFromPipelineByPropertyName=$true,
                   #enabaling this param so I can pass param to function VIA pipeline!
                   ValueFromPipeline=$true,
                   Position=0)]
        $URI,

        #when we pass something here It will turn into True, otherwise False
        [switch] $errorlog,
        [String] $logfile = "C:\powershellUdemy\APIlogs\webscraping_logfile.log"
    )

     Begin
    {
        if($errorlog){ Write-Output "Checking the URL:$URI  status" | Out-File $logfile -Append }
    }
    Process
    { 
           Try{
               
                #Getting status code
                #200 means no Http request error
                $status_code = (Invoke-WebRequest -Uri $URI -UseBasicParsing -DisableKeepAlive).StatusCode

                if ($status_code -eq 200){
                    if($errorlog){ Write-Output "$URI :: $status_code :: GOOD" | Out-File  $logfile -Append -Force;}

                         Write-Host "$URI :: $status_code :: GOOD" -BackgroundColor green -ForegroundColor Yellow
                    
                    } else {
                        if( $errorlog ) { Write-Output "URI :: $status_code :: BAD" | Out-File  $logfile -Append -Force; }
                        
                         Write-Host "$URI :: $status_code :: BAD" -BackgroundColor red -ForegroundColor Yellow
                    }
           }catch{
           $ErrorMessage = $_.Exception.Message
           $FailedItem = $_.Exception.Response
           $status_code= [int]$_.Exception.Response.StatusCode

                if($errorlog){
                Write-Host "$URI :: $status_code :: BAD" -BackgroundColor red -ForegroundColor Yellow
                Write-Output "Something went wrong while checking status of URL : $URI" | Out-File  $logfile -Append -Force;
                Write-Output "ERROR1a# ErrorMessage :  $ErrorMessage StatusCod :: $status_code" | Out-File  $logfile -Append -Force;
                Write-Output "ERROR1a# FailedItem :   $FailedItem StatusCod :: $status_code" | Out-File  $logfile -Append -Force;
                
                }
           }
    }
    End
    {
        #added to return (When using for each only !)
        
        if ($errorlog){ Write-Output "Done with invoking web requst for all URLS `
    ==============$status_code==============="  | Out-File  $logfile -Append -Force;}

        return $status_code
    }

}




### TC - Get request
## ("https://www.google.com","https://www.facebook.com","http://vk.com/sdf")

$arr_Uris = @("https://www.google.com","https://www.facebook.com","http://vk.com/fsw")

$arr_Uris | ForEach-Object {
    
    $URI = $_
        Describe "Given User navigates to $URI"{
            Context "When User sends Get request"{
               [Int32] $statusCode = ($URI | Get-URLStatus -errorlog)
            
                    It 'Then status code should be 200'{
                       $statusCode  | Should be 200
                    }
                }

            }

}
