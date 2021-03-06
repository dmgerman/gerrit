begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|GroupDescription
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
name|extensions
operator|.
name|restapi
operator|.
name|RestResource
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
name|extensions
operator|.
name|restapi
operator|.
name|RestView
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
name|account
operator|.
name|GroupControl
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
name|TypeLiteral
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_class
DECL|class|GroupResource
specifier|public
class|class
name|GroupResource
implements|implements
name|RestResource
block|{
DECL|field|GROUP_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|GroupResource
argument_list|>
argument_list|>
name|GROUP_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|GroupResource
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|control
specifier|private
specifier|final
name|GroupControl
name|control
decl_stmt|;
DECL|method|GroupResource (GroupControl control)
specifier|public
name|GroupResource
parameter_list|(
name|GroupControl
name|control
parameter_list|)
block|{
name|this
operator|.
name|control
operator|=
name|control
expr_stmt|;
block|}
DECL|method|GroupResource (GroupResource rsrc)
name|GroupResource
parameter_list|(
name|GroupResource
name|rsrc
parameter_list|)
block|{
name|this
operator|.
name|control
operator|=
name|rsrc
operator|.
name|getControl
argument_list|()
expr_stmt|;
block|}
DECL|method|getGroup ()
specifier|public
name|GroupDescription
operator|.
name|Basic
name|getGroup
parameter_list|()
block|{
return|return
name|control
operator|.
name|getGroup
argument_list|()
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|getGroup
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
DECL|method|isInternalGroup ()
specifier|public
name|boolean
name|isInternalGroup
parameter_list|()
block|{
name|GroupDescription
operator|.
name|Basic
name|group
init|=
name|getGroup
argument_list|()
decl_stmt|;
return|return
name|group
operator|instanceof
name|GroupDescription
operator|.
name|Internal
return|;
block|}
DECL|method|asInternalGroup ()
specifier|public
name|Optional
argument_list|<
name|GroupDescription
operator|.
name|Internal
argument_list|>
name|asInternalGroup
parameter_list|()
block|{
name|GroupDescription
operator|.
name|Basic
name|group
init|=
name|getGroup
argument_list|()
decl_stmt|;
if|if
condition|(
name|group
operator|instanceof
name|GroupDescription
operator|.
name|Internal
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
operator|(
name|GroupDescription
operator|.
name|Internal
operator|)
name|group
argument_list|)
return|;
block|}
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
DECL|method|getControl ()
specifier|public
name|GroupControl
name|getControl
parameter_list|()
block|{
return|return
name|control
return|;
block|}
block|}
end_class

end_unit

