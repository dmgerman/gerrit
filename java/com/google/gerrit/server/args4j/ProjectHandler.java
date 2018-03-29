begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.args4j
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|args4j
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
name|ProjectUtil
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
name|AuthException
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
name|reviewdb
operator|.
name|client
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
name|server
operator|.
name|permissions
operator|.
name|PermissionBackend
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
name|permissions
operator|.
name|PermissionBackendException
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
name|permissions
operator|.
name|ProjectPermission
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
name|project
operator|.
name|NoSuchProjectException
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
name|project
operator|.
name|ProjectCache
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
name|project
operator|.
name|ProjectState
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
name|assistedinject
operator|.
name|Assisted
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
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|CmdLineException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|CmdLineParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|OptionDef
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|spi
operator|.
name|OptionHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|spi
operator|.
name|Parameters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|spi
operator|.
name|Setter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|ProjectHandler
specifier|public
class|class
name|ProjectHandler
extends|extends
name|OptionHandler
argument_list|<
name|ProjectState
argument_list|>
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ProjectHandler
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
annotation|@
name|Inject
DECL|method|ProjectHandler ( ProjectCache projectCache, PermissionBackend permissionBackend, @Assisted final CmdLineParser parser, @Assisted final OptionDef option, @Assisted final Setter<ProjectState> setter)
specifier|public
name|ProjectHandler
parameter_list|(
name|ProjectCache
name|projectCache
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|CmdLineParser
name|parser
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|OptionDef
name|option
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|Setter
argument_list|<
name|ProjectState
argument_list|>
name|setter
parameter_list|)
block|{
name|super
argument_list|(
name|parser
argument_list|,
name|option
argument_list|,
name|setter
argument_list|)
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|parseArguments (Parameters params)
specifier|public
specifier|final
name|int
name|parseArguments
parameter_list|(
name|Parameters
name|params
parameter_list|)
throws|throws
name|CmdLineException
block|{
name|String
name|projectName
init|=
name|params
operator|.
name|getParameter
argument_list|(
literal|0
argument_list|)
decl_stmt|;
while|while
condition|(
name|projectName
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|projectName
operator|=
name|projectName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|projectName
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|projectName
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
comment|// Be nice and drop the leading "/" if supplied by an absolute path.
comment|// We don't have a file system hierarchy, just a flat namespace in
comment|// the database's Project entities. We never encode these with a
comment|// leading '/' but users might accidentally include them in Git URLs.
comment|//
name|projectName
operator|=
name|projectName
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
name|nameWithoutSuffix
init|=
name|ProjectUtil
operator|.
name|stripGitSuffix
argument_list|(
name|projectName
argument_list|)
decl_stmt|;
name|Project
operator|.
name|NameKey
name|nameKey
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|nameWithoutSuffix
argument_list|)
decl_stmt|;
name|ProjectState
name|state
decl_stmt|;
try|try
block|{
name|state
operator|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|nameKey
argument_list|)
expr_stmt|;
if|if
condition|(
name|state
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CmdLineException
argument_list|(
name|owner
argument_list|,
name|String
operator|.
name|format
argument_list|(
literal|"project %s not found"
argument_list|,
name|nameWithoutSuffix
argument_list|)
argument_list|)
throw|;
block|}
comment|// Hidden projects(permitsRead = false) should only be accessible by the project owners.
comment|// READ_CONFIG is checked here because it's only allowed to project owners(ACCESS may also
comment|// be allowed for other users). Allowing project owners to access here will help them to view
comment|// and update the config of hidden projects easily.
name|ProjectPermission
name|permissionToCheck
init|=
name|state
operator|.
name|statePermitsRead
argument_list|()
condition|?
name|ProjectPermission
operator|.
name|ACCESS
else|:
name|ProjectPermission
operator|.
name|READ_CONFIG
decl_stmt|;
name|permissionBackend
operator|.
name|currentUser
argument_list|()
operator|.
name|project
argument_list|(
name|nameKey
argument_list|)
operator|.
name|check
argument_list|(
name|permissionToCheck
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CmdLineException
argument_list|(
name|owner
argument_list|,
operator|new
name|NoSuchProjectException
argument_list|(
name|nameKey
argument_list|)
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|PermissionBackendException
decl||
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot load project "
operator|+
name|nameWithoutSuffix
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|CmdLineException
argument_list|(
name|owner
argument_list|,
operator|new
name|NoSuchProjectException
argument_list|(
name|nameKey
argument_list|)
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
name|setter
operator|.
name|addValue
argument_list|(
name|state
argument_list|)
expr_stmt|;
return|return
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|getDefaultMetaVariable ()
specifier|public
specifier|final
name|String
name|getDefaultMetaVariable
parameter_list|()
block|{
return|return
literal|"PROJECT"
return|;
block|}
block|}
end_class

end_unit

