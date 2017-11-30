<?php
        include("../Include/base_class.php");
Class  update_availability extends BaseController
    {
       public function __construct() 
       {
           if(!isset($_POST['spot_sn']))// send the sn of spot
           {
               die(json_encode(array('error'=>"Missing Parameters")));
              
           }
           
           $par_set_availability=$_POST['set'];// send either 0 or 1 
          
           switch($par_set_availability){
               case 1:
                   $this->set_available($_POST['spot_sn']);
                   break;
               case 0:
                   $this->set_unavailable($_POST['spot_sn']);
                   break;
               default:
                  die(json_encode(array('error'=>"Wrong spot serial number")));  
                   
           }// end of switch case 
       }// end of constructor
    
    
    private function set_available($spot_sn)
    {
       $par_db=conn::db();
        $par_update_prepare=$par_db->prepare("UPDATE nep_parking_spots SET availability='1' WHERE sn=:ssn ");
        if(!$par_update_prepare->execute(array(':ssn'=>$spot_sn)))
        {
            die(json_encode(array('error'=>"Update error")));
        }
       print(json_encode(array('response'=>"1"))); 
    }
    
    
    private function set_unavailable($spot_sn)
    {
        $par_db=conn::db();
        $par_update_prepare=$par_db->prepare("UPDATE nep_parking_spots SET availability='0' WHERE sn=:ssn ");
        if(!$par_update_prepare->execute(array(':ssn'=>$spot_sn)))
        {
            die(json_encode(array('error'=>"Update error")));
        }
       print(json_encode(array('response'=>"1"))); 
    }
    
    // extend Base Controller
    }
            
$obj=new update_availability();            