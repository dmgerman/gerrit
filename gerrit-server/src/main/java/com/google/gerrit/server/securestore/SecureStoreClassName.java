begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|com.google.gerrit.server.securestore
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|securestore
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
operator|.
name|RUNTIME
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|BindingAnnotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_annotation_defn
annotation|@
name|Retention
argument_list|(
name|RUNTIME
argument_list|)
annotation|@
name|BindingAnnotation
DECL|annotation|SecureStoreClassName
specifier|public
annotation_defn|@interface
name|SecureStoreClassName
block|{}
end_annotation_defn

end_unit

