begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|ElementType
operator|.
name|METHOD
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
operator|.
name|TYPE
import|;
end_import

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
name|common
operator|.
name|annotations
operator|.
name|GwtCompatible
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

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_comment
comment|/**  * A marker for a method that is public solely because it is called from inside a project or an  * organisation using Gerrit.  */
end_comment

begin_annotation_defn
annotation|@
name|Target
argument_list|(
block|{
name|METHOD
block|,
name|TYPE
block|}
argument_list|)
annotation|@
name|Retention
argument_list|(
name|RUNTIME
argument_list|)
annotation|@
name|GwtCompatible
DECL|annotation|UsedAt
specifier|public
annotation_defn|@interface
name|UsedAt
block|{
comment|/** Enumeration of projects that call a method that would otherwise be private. */
DECL|enum|Project
enum|enum
name|Project
block|{
DECL|enumConstant|GOOGLE
name|GOOGLE
block|,
DECL|enumConstant|PLUGIN_DELETE_PROJECT
name|PLUGIN_DELETE_PROJECT
block|,
DECL|enumConstant|PLUGIN_SERVICEUSER
name|PLUGIN_SERVICEUSER
block|,
DECL|enumConstant|PLUGINS_ALL
name|PLUGINS_ALL
block|,
comment|// Use this project if a method/type is generally made available to all plugins.
block|}
comment|/** Reference to the project that uses the method annotated with this annotation. */
DECL|method|value ()
name|Project
name|value
parameter_list|()
function_decl|;
block|}
end_annotation_defn

end_unit

