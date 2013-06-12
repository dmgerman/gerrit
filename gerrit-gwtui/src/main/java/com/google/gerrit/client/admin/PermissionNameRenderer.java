begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.admin
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|admin
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
name|Permission
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|text
operator|.
name|shared
operator|.
name|Renderer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|PermissionNameRenderer
class|class
name|PermissionNameRenderer
implements|implements
name|Renderer
argument_list|<
name|String
argument_list|>
block|{
DECL|field|permissions
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|permissions
decl_stmt|;
static|static
block|{
name|permissions
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|e
range|:
name|Util
operator|.
name|C
operator|.
name|permissionNames
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|permissions
operator|.
name|put
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|permissions
operator|.
name|put
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|fromServer
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fromServer
decl_stmt|;
DECL|method|PermissionNameRenderer (Map<String, String> allFromOutside)
name|PermissionNameRenderer
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|allFromOutside
parameter_list|)
block|{
name|fromServer
operator|=
name|allFromOutside
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|render (String varName)
specifier|public
name|String
name|render
parameter_list|(
name|String
name|varName
parameter_list|)
block|{
if|if
condition|(
name|Permission
operator|.
name|isLabelAs
argument_list|(
name|varName
argument_list|)
condition|)
block|{
return|return
name|Util
operator|.
name|M
operator|.
name|labelAs
argument_list|(
name|Permission
operator|.
name|extractLabel
argument_list|(
name|varName
argument_list|)
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|Permission
operator|.
name|isLabel
argument_list|(
name|varName
argument_list|)
condition|)
block|{
return|return
name|Util
operator|.
name|M
operator|.
name|label
argument_list|(
name|Permission
operator|.
name|extractLabel
argument_list|(
name|varName
argument_list|)
argument_list|)
return|;
block|}
name|String
name|desc
init|=
name|permissions
operator|.
name|get
argument_list|(
name|varName
argument_list|)
decl_stmt|;
if|if
condition|(
name|desc
operator|!=
literal|null
condition|)
block|{
return|return
name|desc
return|;
block|}
name|desc
operator|=
name|fromServer
operator|.
name|get
argument_list|(
name|varName
argument_list|)
expr_stmt|;
if|if
condition|(
name|desc
operator|!=
literal|null
condition|)
block|{
return|return
name|desc
return|;
block|}
name|desc
operator|=
name|permissions
operator|.
name|get
argument_list|(
name|varName
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|desc
operator|!=
literal|null
condition|)
block|{
return|return
name|desc
return|;
block|}
name|desc
operator|=
name|fromServer
operator|.
name|get
argument_list|(
name|varName
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|desc
operator|!=
literal|null
condition|)
block|{
return|return
name|desc
return|;
block|}
return|return
name|varName
return|;
block|}
annotation|@
name|Override
DECL|method|render (String object, Appendable appendable)
specifier|public
name|void
name|render
parameter_list|(
name|String
name|object
parameter_list|,
name|Appendable
name|appendable
parameter_list|)
throws|throws
name|IOException
block|{
name|appendable
operator|.
name|append
argument_list|(
name|render
argument_list|(
name|object
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

