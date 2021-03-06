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
name|gerrit
operator|.
name|server
operator|.
name|CurrentUser
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
DECL|method|projectDefault (ProjectState projectState, CurrentUser user)
specifier|public
specifier|static
name|DashboardResource
name|projectDefault
parameter_list|(
name|ProjectState
name|projectState
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
block|{
return|return
operator|new
name|DashboardResource
argument_list|(
name|projectState
argument_list|,
name|user
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|field|projectState
specifier|private
specifier|final
name|ProjectState
name|projectState
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|CurrentUser
name|user
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
DECL|field|config
specifier|private
specifier|final
name|Config
name|config
decl_stmt|;
DECL|field|projectDefault
specifier|private
specifier|final
name|boolean
name|projectDefault
decl_stmt|;
DECL|method|DashboardResource ( ProjectState projectState, CurrentUser user, String refName, String pathName, Config config, boolean projectDefault)
specifier|public
name|DashboardResource
parameter_list|(
name|ProjectState
name|projectState
parameter_list|,
name|CurrentUser
name|user
parameter_list|,
name|String
name|refName
parameter_list|,
name|String
name|pathName
parameter_list|,
name|Config
name|config
parameter_list|,
name|boolean
name|projectDefault
parameter_list|)
block|{
name|this
operator|.
name|projectState
operator|=
name|projectState
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
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
name|config
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|projectDefault
operator|=
name|projectDefault
expr_stmt|;
block|}
DECL|method|getProjectState ()
specifier|public
name|ProjectState
name|getProjectState
parameter_list|()
block|{
return|return
name|projectState
return|;
block|}
DECL|method|getUser ()
specifier|public
name|CurrentUser
name|getUser
parameter_list|()
block|{
return|return
name|user
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
DECL|method|isProjectDefault ()
specifier|public
name|boolean
name|isProjectDefault
parameter_list|()
block|{
return|return
name|projectDefault
return|;
block|}
block|}
end_class

end_unit

