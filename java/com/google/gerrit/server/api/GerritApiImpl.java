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
DECL|package|com.google.gerrit.server.api
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|api
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
name|api
operator|.
name|GerritApi
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
name|api
operator|.
name|accounts
operator|.
name|Accounts
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
name|api
operator|.
name|changes
operator|.
name|Changes
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
name|api
operator|.
name|config
operator|.
name|Config
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
name|api
operator|.
name|groups
operator|.
name|Groups
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
name|api
operator|.
name|plugins
operator|.
name|Plugins
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
name|api
operator|.
name|projects
operator|.
name|Projects
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
name|Inject
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
name|Singleton
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|GerritApiImpl
class|class
name|GerritApiImpl
implements|implements
name|GerritApi
block|{
DECL|field|accounts
specifier|private
specifier|final
name|Accounts
name|accounts
decl_stmt|;
DECL|field|changes
specifier|private
specifier|final
name|Changes
name|changes
decl_stmt|;
DECL|field|config
specifier|private
specifier|final
name|Config
name|config
decl_stmt|;
DECL|field|groups
specifier|private
specifier|final
name|Groups
name|groups
decl_stmt|;
DECL|field|projects
specifier|private
specifier|final
name|Projects
name|projects
decl_stmt|;
DECL|field|plugins
specifier|private
specifier|final
name|Plugins
name|plugins
decl_stmt|;
annotation|@
name|Inject
DECL|method|GerritApiImpl ( Accounts accounts, Changes changes, Config config, Groups groups, Projects projects, Plugins plugins)
name|GerritApiImpl
parameter_list|(
name|Accounts
name|accounts
parameter_list|,
name|Changes
name|changes
parameter_list|,
name|Config
name|config
parameter_list|,
name|Groups
name|groups
parameter_list|,
name|Projects
name|projects
parameter_list|,
name|Plugins
name|plugins
parameter_list|)
block|{
name|this
operator|.
name|accounts
operator|=
name|accounts
expr_stmt|;
name|this
operator|.
name|changes
operator|=
name|changes
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|groups
operator|=
name|groups
expr_stmt|;
name|this
operator|.
name|projects
operator|=
name|projects
expr_stmt|;
name|this
operator|.
name|plugins
operator|=
name|plugins
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|accounts ()
specifier|public
name|Accounts
name|accounts
parameter_list|()
block|{
return|return
name|accounts
return|;
block|}
annotation|@
name|Override
DECL|method|changes ()
specifier|public
name|Changes
name|changes
parameter_list|()
block|{
return|return
name|changes
return|;
block|}
annotation|@
name|Override
DECL|method|config ()
specifier|public
name|Config
name|config
parameter_list|()
block|{
return|return
name|config
return|;
block|}
annotation|@
name|Override
DECL|method|groups ()
specifier|public
name|Groups
name|groups
parameter_list|()
block|{
return|return
name|groups
return|;
block|}
annotation|@
name|Override
DECL|method|projects ()
specifier|public
name|Projects
name|projects
parameter_list|()
block|{
return|return
name|projects
return|;
block|}
annotation|@
name|Override
DECL|method|plugins ()
specifier|public
name|Plugins
name|plugins
parameter_list|()
block|{
return|return
name|plugins
return|;
block|}
block|}
end_class

end_unit

