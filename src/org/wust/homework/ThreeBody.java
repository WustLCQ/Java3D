package org.wust.homework;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.net.URL;
import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.PointLight;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.prism.paint.Color;

import sun.font.EAttribute;

public class ThreeBody extends Applet{
	public static void main(String[] args) {
		new MainFrame(new ThreeBody(), 640, 480);
	}
	
	public void init() {
		GraphicsConfiguration graphicsConfiguration = 
				SimpleUniverse.getPreferredConfiguration();
		//构造Canvas3D对象
		Canvas3D canvas3d = new Canvas3D(graphicsConfiguration);
		setLayout(new BorderLayout());
		add(canvas3d, BorderLayout.CENTER);
		//创建场景分支子图
		BranchGroup branchGroup = createSceneGraph();
		branchGroup.compile();
		//创建并设置SimpleUniverse对象
		SimpleUniverse simpleUniverse = new SimpleUniverse(canvas3d);
		simpleUniverse.getViewingPlatform().setNominalViewingTransform();
		simpleUniverse.addBranchGraph(branchGroup);
	}

	private BranchGroup createSceneGraph() {
		//创建场景分支图
		BranchGroup root = new BranchGroup();
		Transform3D transform3D = new Transform3D();
        transform3D.setTranslation(new Vector3d(0, 0, -7));
        TransformGroup transformGroup = new TransformGroup();
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transformGroup.setTransform(transform3D);
        root.addChild(transformGroup);
        
        //太阳
        Transform3D sunTrans = new Transform3D();
        sunTrans.setTranslation(new Vector3f(0f, 0f, 0f));
        TransformGroup sunTG = new TransformGroup();
        sunTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        sunTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        sunTG.setTransform(sunTrans);
        transformGroup.addChild(sunTG);
        
        //创建场景物体
        String sun = "images/sun.jpg";
        Appearance appearance = createTextureAppearance(sun);	//生成具有纹理的外观形象
        Sphere sphere = new Sphere(2f, Primitive.GENERATE_TEXTURE_COORDS, 50, appearance);
        sunTG.addChild(sphere);

		// 地球
		Transform3D earthTrans = new Transform3D();
		earthTrans.setTranslation(new Vector3f(0f, 0f, 3f));
		TransformGroup earthTG = new TransformGroup();
		earthTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		earthTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		earthTG.setTransform(earthTrans);
		sunTG.addChild(earthTG);

		// 创建场景物体
		String earth = "images/earth.jpg";
		appearance = createTextureAppearance(earth); // 生成具有纹理的外观形象
		sphere = new Sphere(0.7f, Primitive.GENERATE_TEXTURE_COORDS, 50, appearance);
		earthTG.addChild(sphere);

		// 旋转
		Alpha alpha = new Alpha(-1, 6000);
		RotationInterpolator rotationInterpolator = new RotationInterpolator(alpha, earthTG, earthTrans, 0, 2*(float)Math.PI);
		BoundingSphere bounds = new BoundingSphere();
		rotationInterpolator.setSchedulingBounds(bounds);
        earthTG.addChild(rotationInterpolator);
        
		// 月球
		Transform3D moonTrans = new Transform3D();
		moonTrans.setTranslation(new Vector3f(0f, 0f, 3f));
		TransformGroup moonTG = new TransformGroup();
		moonTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		moonTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		moonTG.setTransform(moonTrans);
		earthTG.addChild(moonTG);

		// 创建场景物体
		String moon = "images/moon.jpg";
		appearance = createTextureAppearance(moon); // 生成具有纹理的外观形象
		sphere = new Sphere(0.2f, Primitive.GENERATE_TEXTURE_COORDS, 50, appearance);
		moonTG.addChild(sphere);

		 //旋转
		Alpha alphaMoon = new Alpha(-1, 6000);
		RotationInterpolator riMoon = new RotationInterpolator(alphaMoon, moonTG, moonTrans, 0,
				2 * (float) Math.PI);
		rotationInterpolator.setSchedulingBounds(bounds);
		moonTG.addChild(riMoon);

		// 添加背景
		Background background = new Background(0f, 0f, 0f);
		background.setApplicationBounds(bounds);
		root.addChild(background);

		// 设置鼠标转动
		MouseRotate mouseRotate = new MouseRotate();
		mouseRotate.setTransformGroup(transformGroup);
		mouseRotate.setSchedulingBounds(bounds);
		transformGroup.addChild(mouseRotate);
		// 设置鼠标平移
		MouseTranslate mouseTranslate = new MouseTranslate();
		mouseTranslate.setTransformGroup(transformGroup);
		mouseTranslate.setSchedulingBounds(bounds);
		transformGroup.addChild(mouseTranslate);
		// 设置鼠标缩放
		MouseWheelZoom mouseZoom = new MouseWheelZoom();
		mouseZoom.setTransformGroup(transformGroup);
		mouseZoom.setSchedulingBounds(bounds);
		transformGroup.addChild(mouseZoom);
		
		//添加光源
		PointLight pointLight = new PointLight(new Color3f(1.0f, 1.0f, 1.0f), 
				new Point3f(0f, 0f, 1f/(float)Math.tan(Math.PI/8)), 
				new Point3f(1f, 0f, 0f));
		pointLight.setInfluencingBounds(bounds);
		root.addChild(pointLight);
		
        return root;
	}

	private Appearance createTextureAppearance(String file) {
		Appearance appearance = new Appearance();
		URL filname = getClass().getClassLoader().getResource(file);
		TextureLoader textureLoader = new TextureLoader(filname, this);	//纹理载入
		ImageComponent2D imageComponent2D = textureLoader.getImage();
		
		if(imageComponent2D == null) {
			System.out.println("Can't find texture file!");
		}
		
		Texture2D texture2d = new Texture2D(Texture.BASE_LEVEL, 
				Texture.RGBA, imageComponent2D.getWidth(), imageComponent2D.getHeight());
		texture2d.setImage(0, imageComponent2D);	//设置纹理的图象
		texture2d.setEnable(true);
		texture2d.setMagFilter(Texture.BASE_LEVEL_LINEAR);
		texture2d.setMinFilter(Texture.BASE_LEVEL_LINEAR);
		appearance.setTexture(texture2d);
		
		return appearance;
	}
}
