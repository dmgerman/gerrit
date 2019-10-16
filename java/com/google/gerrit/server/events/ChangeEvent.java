begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.events
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|events
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Supplier
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
operator|.
name|Change
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
operator|.
name|RefNames
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|data
operator|.
name|ChangeAttribute
import|;
end_import

begin_class
DECL|class|ChangeEvent
specifier|public
specifier|abstract
class|class
name|ChangeEvent
extends|extends
name|RefEvent
block|{
DECL|field|change
specifier|public
name|Supplier
argument_list|<
name|ChangeAttribute
argument_list|>
name|change
decl_stmt|;
DECL|field|project
specifier|public
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|field|refName
specifier|public
name|String
name|refName
decl_stmt|;
DECL|field|changeKey
specifier|public
name|Change
operator|.
name|Key
name|changeKey
decl_stmt|;
DECL|method|ChangeEvent (String type, Change change)
specifier|protected
name|ChangeEvent
parameter_list|(
name|String
name|type
parameter_list|,
name|Change
name|change
parameter_list|)
block|{
name|super
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|this
operator|.
name|project
operator|=
name|change
operator|.
name|getProject
argument_list|()
expr_stmt|;
name|this
operator|.
name|refName
operator|=
name|RefNames
operator|.
name|fullName
argument_list|(
name|change
operator|.
name|getDest
argument_list|()
operator|.
name|branch
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|changeKey
operator|=
name|change
operator|.
name|getKey
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getProjectNameKey ()
specifier|public
name|Project
operator|.
name|NameKey
name|getProjectNameKey
parameter_list|()
block|{
return|return
name|project
return|;
block|}
annotation|@
name|Override
DECL|method|getRefName ()
specifier|public
name|String
name|getRefName
parameter_list|()
block|{
return|return
name|refName
return|;
block|}
DECL|method|getChangeKey ()
specifier|public
name|Change
operator|.
name|Key
name|getChangeKey
parameter_list|()
block|{
return|return
name|changeKey
return|;
block|}
block|}
end_class

end_unit

