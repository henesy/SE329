<?php
        include("../Include/base_class.php");
Class  Nearest extends BaseController
    {
   
       public function __construct() 
       {
           $par_long=(isset($_POST['longitude'])?$_POST['longitude']:-93.6498156);
           $par_lat=(isset($_POST['latitude'])?$_POST['latitude']:42.0258791);
           if(isset($_POST['category_sn']))//only for categorized
           {
               
            $this->categorized($par_lat,$par_long,trim($_POST['category_sn']));
               
           }
           else{
               
               $this->uncategorized($par_lat,$par_long);
           }
           
       }
    
    private function uncategorized($lat,$lon) // prints json array of all categories
    {
        $par_db=conn::db(); // connects to database PDO WAY
        $par_select=$par_db->prepare("SELECT *,
                      111.045* DEGREES(ACOS(COS(RADIANS(:lat)) *
                          COS(RADIANS(latitude)) *
                          COS(RADIANS(:long) - RADIANS(longitude)) +
                          SIN(RADIANS(:lat)) * SIN(RADIANS(latitude))))
                          AS distance_in_km FROM nep_parking_spots JOIN
                          ( SELECT 42.81 AS :lat, -70.81 AS :long ) AS p ON 1=1 where availability=1 ORDER BY distance_in_km");
        $par_select->execute(array(':lat'=>$lat,':long'=>$lon));
        $par_arr_fetch=$par_select->fetchAll();
        
        print(json_encode($par_arr_fetch));
        
    }
    
    private function categorized($lat,$lon,$category_sn)// prints the json array of particular according to shortest siatance
    {
        $par_db=conn::db(); // connects to database PDO WAY
        $par_select=$par_db->prepare("SELECT *,
                      111.045* DEGREES(ACOS(COS(RADIANS(:lat)) *
                          COS(RADIANS(latitude)) *
                          COS(RADIANS(:long) - RADIANS(longitude)) +
                          SIN(RADIANS(:lat)) * SIN(RADIANS(latitude))))
                          AS distance_in_km FROM nep_parking_spots JOIN
                          ( SELECT 42.81 AS :lat, -70.81 AS :long ) AS p ON 1=1 WHERE category_sn=:csn and availability=1 ORDER BY distance_in_km");
        $par_select->execute(array(':lat'=>$lat,':long'=>$lon,':csn'=>$category_sn));
        $par_arr_fetch=$par_select->fetchAll();
        
        print(json_encode($par_arr_fetch));
        
    }
    
    
    // extend Base Controller
    }
            
$obj=new Nearest();            