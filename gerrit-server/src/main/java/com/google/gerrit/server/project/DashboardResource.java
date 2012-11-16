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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
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
name|inject
operator|.
name|TypeLiteral
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_class
DECL|class|DashboardResource
specifier|public
class|class
name|DashboardResource
implements|implements
name|RestResource
block|{
DECL|field|DASHBOARD_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|DashboardResource
argument_list|>
argument_list|>
name|DASHBOARD_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|DashboardResource
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|control
specifier|private
specifier|final
name|ProjectControl
name|control
decl_stmt|;
DECL|field|refName
specifier|private
specifier|final
name|String
name|refName
decl_stmt|;
DECL|field|pathName
specifier|private
specifier|final
name|String
name|pathName
decl_stmt|;
DECL|field|objId
specifier|private
specifier|final
name|ObjectId
name|objId
decl_stmt|;
DECL|field|config
specifier|private
specifier|final
name|Config
name|config
decl_stmt|;
DECL|method|DashboardResource (ProjectControl control, String refName, String pathName, ObjectId objId, Config config)
name|DashboardResource
parameter_list|(
name|ProjectControl
name|control
parameter_list|,
name|String
name|refName
parameter_list|,
name|String
name|pathName
parameter_list|,
name|ObjectId
name|objId
parameter_list|,
name|Config
name|config
parameter_list|)
block|{
name|this
operator|.
name|control
operator|=
name|control
expr_stmt|;
name|this
operator|.
name|refName
operator|=
name|refName
expr_stmt|;
name|this
operator|.
name|pathName
operator|=
name|pathName
expr_stmt|;
name|this
operator|.
name|objId
operator|=
name|objId
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
DECL|method|getControl ()
specifier|public
name|ProjectControl
name|getControl
parameter_list|()
block|{
return|return
name|control
return|;
block|}
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
DECL|method|getPathName ()
specifier|public
name|String
name|getPathName
parameter_list|()
block|{
return|return
name|pathName
return|;
block|}
DECL|method|getObjectId ()
specifier|public
name|ObjectId
name|getObjectId
parameter_list|()
block|{
return|return
name|objId
return|;
block|}
DECL|method|getConfig ()
specifier|public
name|Config
name|getConfig
parameter_list|()
block|{
return|return
name|config
return|;
block|}
block|}
end_class

end_unit

