import java.util.Scanner;

class IPChecker{
    boolean ipv4(String ip){
        boolean checkPass = true;
        //由於ipv4 和 6 最直接差異在 字節用'.'或':'間隔
        if(ip.indexOf(".") == -1)
            checkPass = false;        
        else{
            int sum = 0,      //加總每個 Octet 的數值  0~255
                octetLen = 0; //每個 Octec內的長度
            for(int i = 0 ; i < ip.length(); ){
                //處理 數字為0開頭
                if(ip.charAt(i) == 48 && i + 1 < ip.length() && (ip.charAt(i + 1) >= 48 && ip.charAt(i + 1) <= 57)){
                    checkPass = false;
                    break;
                }
                //處理 數字範圍( sum ) 一路算到沒遇到數字 或 跑完IP字串
                while(i < ip.length() && ip.charAt(i) >= 48 && ip.charAt(i) <= 57){
                        octetLen += 1;
                        sum *= 10;
                        sum += ip.charAt(i) - 48;
                        i++;
                }
                //處理 正常數字結尾 i==length()的狀況
                if(i == ip.length()){
                    if(sum <= 255 && sum >= 0)
                        break;
                    else{
                        checkPass = false;
                        break;
                    }
                }
                //處理 特殊狀況 192.168.1.  以點結尾 若不處理 會誤判為true
                if(ip.charAt(i) == '.' && ((i + 1) == ip.length() )){
                    checkPass = false;
                    break;
                }
                //處理 遇到其他非數字非.符號  或 數字超過範圍  
                if(ip.charAt(i) != '.' || sum > 255 ){
                    checkPass = false;
                    break;
                }
                //處理 遇到. 可是 .前面為空的狀況
                else if(ip.charAt(i) == '.' && octetLen == 0){
                    checkPass = false;
                    break;
                }
                //一切合格的狀況 遇到.
                else{
                    octetLen = 0;
                    sum = 0;
                    i++;
                }
            }//end of for in line.14
        }//end of else in line.11
        return checkPass;
    }//end of ipv4()
    
    /*搞了好幾小時 終於correct 特別要注意幾種狀況 
      1. 有:: 則 octet 不能 >= 8  
      2. 沒有:: 則 octet 要 = 8
      3. 結尾不能是 :
    */
    boolean ipv6(String ip){
        boolean checkPass = true;
        int octet = 0, //有幾個octet
            doubleColon = 0,//用雙冒號(縮寫連續0)的次數 最多1
            octetLen = 0; //每個Octet內的長度
        for(int i = 0; i < ip.length(); ){
            //處理 開頭雙冒號
            if(i == 0 && ip.charAt(i) == ':' && ip.charAt(i + 1) == ':'){
                octet += 1; // 遇到雙冒號 至少縮寫一節
                doubleColon += 1;
                i += 2;
                continue;
            }
            //處理 當index在字串範圍內 而且 如果字串字元符合範圍 0~9/A~F/a~f
            while(i < ip.length() &&
                  ((ip.charAt(i) >= 48 && ip.charAt(i) <= 57) ||
                   (ip.charAt(i) >= 65 && ip.charAt(i) <= 70) ||
                   (ip.charAt(i) >= 97 && ip.charAt(i) <= 102)
                  )){
                    octetLen += 1;
                    i++;
                 }
            //octet內長度 不在1~4   
            if(octetLen <= 0 || octetLen > 4){
               checkPass = false;
               break;
            }
            else if(i != ip.length() && ip.charAt(i) != ':'){
                checkPass = false;
                break;
            }
            //途中遇到雙冒號
            else if(i + 1 < ip.length() && ip.charAt(i + 1) == ':'){
                if(doubleColon == 1){
                    checkPass = false;
                    break;
                }
                else{
                    octet += 1; // 遇到雙冒號 至少縮寫一節
                    octetLen = 0;
                    doubleColon += 1;
                    i += 2;
                }
            }
            // 以:結尾 而且不是雙冒號
            else if(i + 1 == ip.length() && ip.charAt(i) == ':'){
                checkPass = false;
                break;
            }
            // 一切合格跑完IP字串長度 且以 0~9/A~F/a~f 結尾
            else if(i == ip.length()){
                octet += 1;
                break;
            }
            // 一切合格跑完一個Octet且ip.charAt(i)為 ;
            else{
                octet += 1;
                octetLen = 0;
                i++;
            }
        }//end of for in line.55
        if(doubleColon == 0 && octet != 8)
            checkPass = false;
        else if(doubleColon == 1 && octet >= 8)
            checkPass = false;
        return checkPass;
    }//end of ipv6()
}//end of class IPChecker

public class Main{
    public static void main(String[] argv){
        Scanner sc = new Scanner(System.in);
        IPChecker ipc = new IPChecker();        
        String data = sc.nextLine();
        if((data.length() - data.replace(".","").length()) != 3){
          if(ipc.ipv6(data))
            System.out.println("IPv6");
          else
            System.out.println("不是合法的IP");
        }
        else{
          if(ipc.ipv4(data))
            System.out.println("IPv4");
          else
            System.out.println("不是合法的IP");
        }
    }
}
