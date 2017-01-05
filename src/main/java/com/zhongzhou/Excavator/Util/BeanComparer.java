package com.zhongzhou.Excavator.Util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.javascript.host.Iterator;
import com.zhongzhou.Excavator.Annotation.BeanComparableProp;
import com.zhongzhou.Excavator.Annotation.BeanComparePropId;

public class BeanComparer {
	
	private static final String NEW = "{new}";
	
	private static final String REMOVE = "{remove}";
	
	private int recursionIndex = 0;

	public <T> List<String> beanDataChanged( final T newBean , final T oldBean ) throws IllegalArgumentException, IllegalAccessException {
		
		if( this.recursionIndex > 10){
			throw new RuntimeException( "Bean Comparer is out of recurison index." );
		}
		this.recursionIndex ++;

		if( newBean == null && oldBean == null ){
			return new ArrayList<String>(0);
		}
		if( newBean != null && oldBean == null ){
			return new ArrayList<String>( Arrays.asList( "this:" + NEW ) );
		}
		if( newBean == null && oldBean != null ){
			return new ArrayList<String>( Arrays.asList( "this:"+REMOVE ) );
		}
		
		Class TClass = newBean.getClass();
		
		List<String> allChanged = new ArrayList<String>();
		
		Field[] fieldlist = TClass.getDeclaredFields();
		
		for( Field field : fieldlist ){
			
			if( field.isAnnotationPresent(BeanComparableProp.class) ){
				
                for ( Annotation anno : field.getDeclaredAnnotations() ) {

                    if(anno.annotationType().equals(BeanComparableProp.class) ){
                    	
                    	boolean isChanged = false; 
                    	
            			String fieldName = field.getName();
                    	
                    	field.setAccessible(true);
            	        Object valOld = field.get(oldBean);
            	        Object valNew = field.get(newBean);
            	        
            	        if( valNew == null && valOld==null ){
            	        	
            	        }else if( valNew == null && valOld!=null ){
            	        	allChanged.add( fieldName + ":" + REMOVE );
                	    }else if( valNew != null && valOld==null ){
                	    	allChanged.add( fieldName + ":" + NEW );
                	    }else{
                	        if( !isChanged && valOld.getClass().isPrimitive() ){
                	        	if( !valOld.equals( valNew ) ) {
                    	        	isChanged = true;
                    	        }
                	        }else if( !isChanged && ((BeanComparableProp)anno).isRecursion()  ){
                	        		
                	        	if( valOld instanceof List && valNew instanceof List ){
            	        			
                	        		List<String> listChanged = this.listDataChanged( (List)valNew, (List)valOld );
        	        				
        	        				if( listChanged.size() > 0 ){
        	        					for( String changed : listChanged ){
        	        						allChanged.add( field.getName() + changed );
        	        					}
        	        				}
                	        	}else if( valOld instanceof Map && valNew instanceof Map  ){
            	        			
            	        			List<String> listChanged = this.mapDataChanged( (Map)valNew, (Map)valOld );
            	        				
            	        			if( listChanged.size() > 0 ){
            	        				for( String changed : listChanged ){
            	        					allChanged.add( field.getName() + changed );
            	        				}
            	        			}
                	        	}else{
                	        		
                	        		List<String> listChanged = this.beanDataChanged(valNew, valOld);
                	        		
                	        		if( listChanged.size() > 0 ){
            	        				for( String changed : listChanged ){
            	        					allChanged.add( field.getName() + "." + changed );
            	        				}
            	        			}
                	        	}
            	        	}else if( !isChanged ) {
            	        		if( !valOld.equals( valNew ) ) {
                    	        	isChanged = true;
                    	        }
                	        }
                	        
                	        if( isChanged ){
                	        	//allChanged.add( fieldName );
                	        	allChanged.add( fieldName + ":{\"old\":\"" + valOld + "\", \"new\":\"" + valNew + "\"}");
                	        }
            	        }
            	       
            	        break;
                    }
                }
			}
		}
		
		this.recursionIndex --;
		return allChanged;
	}
	
	
	public <T> List<String> listDataChanged(  final List<T> newBeans , final List<T> oldBeans ) throws RuntimeException, IllegalAccessException{
		
		if( this.recursionIndex > 10){
			throw new RuntimeException( "Bean Comparer is out of recurison index." );
		}
		this.recursionIndex ++;
		
		Class TClass = null;
		if( newBeans.size() > 0 ){
			TClass = newBeans.get(0).getClass();
		}else if( oldBeans.size() > 0 ){
			TClass = oldBeans.get(0).getClass();
		}
		if( TClass == null ){
			return new ArrayList<String>(0);
		}
		
		List<String> allChanged = new ArrayList<String>();
		
		List<Field> compareIds = new  ArrayList<Field>();
		
		Field[] fieldlist = TClass.getDeclaredFields();
		
		for( Field field : fieldlist ){
			
			if( field.isAnnotationPresent(BeanComparePropId.class) ){
				
				compareIds.add( field );
			}
		}
		
		/* 没有compare Id 就是逐行比对 */
		if( compareIds.size() < 1 ){
			
			int oldBeanSize = oldBeans.size();
			int newBeanSize = newBeans.size();
			int compareIndex = newBeanSize > oldBeanSize ? oldBeanSize : newBeanSize;
			int sizeSubtraction = newBeanSize - oldBeanSize;
			for( int i=0 ; i< compareIndex ; i++ ){
				
				List<String> subChanged = beanDataChanged( newBeans.get(i), oldBeans.get(i) );
				for( String changed : subChanged ){
					allChanged.add( "[" + i + "]." + changed );
				}
			}
			for( int i=compareIndex ; i < compareIndex - ( sizeSubtraction ) ; i++ ){
				
				if( sizeSubtraction  < 0 ) {
					
					allChanged.add( "[" + i + "]:" + REMOVE );
				}else{
					
					allChanged.add( "[" + i + "]:" + NEW );
				}
			}
		}
		
		/* 根据id尝试找到相同的对应对象 */
		if( compareIds.size() > 0 ){
		
			boolean[] isRemove = new boolean[ oldBeans.size() ];
			for( int i=0; i< oldBeans.size() ; i++ ){
				isRemove[i] = true;
			}
			int index = 0;
			for( T newBean : newBeans ){
				
				boolean isNew = true;
				int oldBeansIndex = 0;
				for( T oldBean : oldBeans ){
					
					Object valOld = compareIds.get(0).get(oldBean);
			        Object valNew = compareIds.get(0).get(newBean);
			        if( valOld!=null && valOld.equals( valNew ) ){
			        	
			        	boolean allEqual = true;
			        	for( Field field : compareIds ){
			        		
			        		valOld = field.get(oldBean);
					        valNew = field.get(newBean);
					        
					        if( valOld == null ){
					        	if( valNew != null ) allEqual = false;
					        }else if( !valOld.equals( valNew ) ){
					        	allEqual = false;
					        }
			        	}
			        	if( allEqual ){
			        		isNew = false;
			        		isRemove[oldBeansIndex] = false;
			        		List<String> subChanged = beanDataChanged( newBean, oldBean );
							for( String changed : subChanged ){
								allChanged.add( "[" + index + "]." + changed );
							}
			        		break;
			        	}
			        }
			        oldBeansIndex ++;
				}
				if( isNew ){
					allChanged.add( "[" + index + "]:" + NEW );
				}
				index ++;
			}
			for( int i=0; i< isRemove.length ; i++ ){
				if( isRemove[i] ){
					allChanged.add( "[" + i + "]:" + REMOVE );
				}
			}
		}
		
		this.recursionIndex --;
		return allChanged;
	}
	
	public <K,T> List<String> mapDataChanged( final Map<K,T> newBeans , final Map<K,T> oldBeans ) throws RuntimeException, IllegalAccessException{
		
		if( this.recursionIndex > 10){
			throw new RuntimeException( "Bean Comparer is out of recurison index." );
		}
		this.recursionIndex ++;
		
		Class KClass = null;
		if( newBeans.keySet().size() > 0 ){
			Iterator iterator = (Iterator) newBeans.keySet().iterator();
			while ( newBeans.keySet().iterator().hasNext() ){
				Object next = iterator.next();
				if( next != null ){
					KClass = next.getClass();
					break;
				}
			}
		}else if( oldBeans.values().size() > 0 ){
			Iterator iterator = (Iterator) newBeans.keySet().iterator();
			while ( newBeans.keySet().iterator().hasNext() ){
				Object next = iterator.next();
				if( next != null ){
					KClass = next.getClass();
					break;
				}
			}
		}
		if( KClass == null ){
			return new ArrayList<String>(0);
		}
		
		List<String> allChanged = new ArrayList<String>();
			
		boolean[] isRemove = new boolean[ oldBeans.size() ];
		for( int i=0; i< oldBeans.size() ; i++ ){
			isRemove[i] = true;
		}
		for( K newKey : newBeans.keySet() ){
			
			if( oldBeans.containsKey( newKey ) ){
				List<String> subChanged = beanDataChanged( newBeans.get(newKey), oldBeans.get(newKey) );
				for( String changed : subChanged ){
					allChanged.add( "[" + newKey + "]." + changed );
				}
			}else{
				allChanged.add( "[" + newKey + "]:" + NEW );
			}
		}
		for( K oldKey : oldBeans.keySet() ){
			if( !newBeans.containsKey( oldKey ) ){
				allChanged.add( "[" + oldKey + "]:" + REMOVE );
			}
		}
		
		this.recursionIndex --;
		return allChanged;
	}
}
