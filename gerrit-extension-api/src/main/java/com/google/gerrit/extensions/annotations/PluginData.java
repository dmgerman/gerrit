begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.extensions.annotations
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|annotations
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

begin_comment
comment|/**  * Local path where a plugin can store its own private data.  *  *<p>A plugin or extension may receive this string by Guice injection to discover a directory where  * it can store configuration or other data that is private:  *  *<p>This binding is on both {@link java.io.File} and {@link java.nio.file.Path}, pointing to the  * same location. The {@code File} version should be considered deprecated and may be removed in a  * future version.  *  *<pre>  * {@literal @Inject}  * MyType(@PluginData java.nio.file.Path myDir) {  *   this.in = Files.newInputStream(myDir.resolve(&quot;my.config&quot;));  * }  *</pre>  */
end_comment

begin_annotation_defn
annotation|@
name|Retention
argument_list|(
name|RUNTIME
argument_list|)
annotation|@
name|BindingAnnotation
DECL|annotation|PluginData
specifier|public
annotation_defn|@interface
name|PluginData
block|{}
end_annotation_defn

end_unit

