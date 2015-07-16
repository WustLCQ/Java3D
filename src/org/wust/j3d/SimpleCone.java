package org.wust.j3d;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.universe.*;
import com.sun.prism.Graphics;
import javax.media.j3d.*;
import javax.vecmath.*;

public class SimpleCone extends Applet{
	/**
	 * @return
	 */
	public BranchGroup createSceneGraph() {
		BranchGroup objRoot = new BranchGroup();
			
		//给背景、灯光等对象设置有效范围
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
		
		//生成画面的白色背景
		Color3f bgColor = new Color3f(1.0f, 1.0f, 1.0f);
		Background bg = new Background(bgColor);
		bg.setApplicationBounds(bounds);
		objRoot.addChild(bg);
		
		Color3f directionalColor = new Color3f(1.f, 1.f, 1.f);
		Vector3f vec = new Vector3f(0.f, 0.f, -1.0f);
		DirectionalLight directionalLight = new DirectionalLight(directionalColor, vec);
		directionalLight.setInfluencingBounds(bounds);
		objRoot.addChild(directionalLight);
		
		//设置变换
		Transform3D transform3D = new Transform3D();
        transform3D.setTranslation(new Vector3d(0, 0, -7));
        TransformGroup transformGroup = new TransformGroup();
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transformGroup.setTransform(transform3D);
		objRoot.addChild(transformGroup);
		
		//设置鼠标转动
		MouseRotate mouseRotate = new MouseRotate();
        mouseRotate.setTransformGroup(transformGroup);
        mouseRotate.setSchedulingBounds(bounds);
        transformGroup.addChild(mouseRotate);
		//设置鼠标平移
        MouseTranslate mouseTranslate = new MouseTranslate();
        mouseTranslate.setTransformGroup(transformGroup);
        mouseTranslate.setSchedulingBounds(bounds);
        transformGroup.addChild(mouseTranslate);
		//设置鼠标缩放
		MouseWheelZoom mouseZoom = new MouseWheelZoom();
        mouseZoom.setTransformGroup(transformGroup);
        mouseZoom.setSchedulingBounds(bounds);
        transformGroup.addChild(mouseZoom);
		
		//给对象设定外观材质
		Appearance app = new Appearance();
		Material material = new Material();
		material.setDiffuseColor(new Color3f(1.0f, 0.0f, 0.0f));
		app.setMaterial(material);
		
		//生成一个圆锥
		Cone cone = new Cone(1f, 2f, 1, app);
		transformGroup.addChild(cone);
		
		objRoot.compile();
		return objRoot;
	}
	
	public SimpleCone() {
		setLayout(new BorderLayout());
		GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
		Canvas3D c = new Canvas3D(gc);
		add("Center", c);
		BranchGroup scene = createSceneGraph();
		SimpleUniverse u = new SimpleUniverse(c);
		u.getViewingPlatform().setNominalViewingTransform();
		u.addBranchGraph(scene);
	}
	
	public static void main(String[] args) {
		new MainFrame(new SimpleCone(), 400, 300);
	}
}
