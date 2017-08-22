begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.sshd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
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
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
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
comment|/**  * Annotation tagged on a concrete Command to describe what it is doing and whether it can be run on  * slaves.  */
end_comment

begin_annotation_defn
annotation|@
name|Target
argument_list|(
block|{
name|ElementType
operator|.
name|TYPE
block|}
argument_list|)
annotation|@
name|Retention
argument_list|(
name|RUNTIME
argument_list|)
DECL|annotation|CommandMetaData
specifier|public
annotation_defn|@interface
name|CommandMetaData
block|{
DECL|enum|Mode
enum|enum
name|Mode
block|{
DECL|enumConstant|MASTER
name|MASTER
block|,
DECL|enumConstant|MASTER_OR_SLAVE
name|MASTER_OR_SLAVE
block|;
DECL|method|isSupported (boolean slaveMode)
specifier|public
name|boolean
name|isSupported
parameter_list|(
name|boolean
name|slaveMode
parameter_list|)
block|{
return|return
name|this
operator|==
name|MASTER_OR_SLAVE
operator|||
operator|!
name|slaveMode
return|;
block|}
block|}
DECL|method|name ()
name|String
name|name
parameter_list|()
function_decl|;
DECL|method|description ()
name|String
name|description
parameter_list|()
default|default
literal|""
function_decl|;
DECL|method|runsAt ()
DECL|field|Mode.MASTER
name|Mode
name|runsAt
parameter_list|()
default|default
name|Mode
operator|.
name|MASTER
function_decl|;
block|}
end_annotation_defn

end_unit

