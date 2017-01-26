package silva.miguel.throwyourlife;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import java.io.Serializable;

/**
 * Created by Miguel on 05/07/2016.
 */
public class MainThread extends Thread implements Serializable{
    //Variaveis de instancia
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    private Canvas canvas;

    //Construtor
    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    //Métodos
    @Override
    public void run(){
        long startTime;
        long timeMillis, waitTime;
        long targetTime = 1000/ Constants.FPS;
        while(running){
            startTime = System.nanoTime();
            canvas = null;
            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    gamePanel.update();
                    gamePanel.draw(canvas);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            finally{
                if(canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch (Exception e) {e.printStackTrace();}
                }
            }
            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMillis;
            try{
                this.sleep(waitTime);
            }catch(Exception e){}
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean bool){
        running = bool;
    }
}
