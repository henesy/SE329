<?php
        include("../Include/base_class.php");
Class  Category_list extends BaseController
    {
       public function __construct() 
       {
         $this->category_list();  
       }
    // extend Base Controller
      private function category_list()
      {
          $par_db=conn::db();
          $par_prepare=$par_db->prepare("SELECT * FROM nep_parking_lot_category");
          if(!$par_prepare->execute())
          {
              die(json_encode(array('error'=>"Unable to process.")));
          }
          $par_arr_data=$par_prepare->fetchAll();
          print(json_encode($par_arr_data));
      }
    }
            
$obj=new Category_list();            