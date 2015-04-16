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
DECL|package|com.google.gerrit.server.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|util
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
name|reviewdb
operator|.
name|client
operator|.
name|Branch
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
name|reviewdb
operator|.
name|client
operator|.
name|SubmoduleSubscription
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
name|git
operator|.
name|GitRepositoryManager
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|BlobBasedConfig
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
name|Constants
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * It parses from a configuration file submodule sections.  *<p>  * Example of submodule sections:  *  *<pre>  * [submodule "project-a"]  *     url = http://localhost/a  *     path = a  *     branch = .  *  * [submodule "project-b"]  *     url = http://localhost/b  *     path = b  *     branch = refs/heads/test  *</pre>  */
end_comment

begin_class
DECL|class|SubmoduleSectionParser
specifier|public
class|class
name|SubmoduleSectionParser
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (BlobBasedConfig bbc, String thisServer, Branch.NameKey superProjectBranch)
name|SubmoduleSectionParser
name|create
parameter_list|(
name|BlobBasedConfig
name|bbc
parameter_list|,
name|String
name|thisServer
parameter_list|,
name|Branch
operator|.
name|NameKey
name|superProjectBranch
parameter_list|)
function_decl|;
block|}
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|bbc
specifier|private
specifier|final
name|BlobBasedConfig
name|bbc
decl_stmt|;
DECL|field|thisServer
specifier|private
specifier|final
name|String
name|thisServer
decl_stmt|;
DECL|field|superProjectBranch
specifier|private
specifier|final
name|Branch
operator|.
name|NameKey
name|superProjectBranch
decl_stmt|;
annotation|@
name|Inject
DECL|method|SubmoduleSectionParser (GitRepositoryManager repoManager, @Assisted BlobBasedConfig bbc, @Assisted String thisServer, @Assisted Branch.NameKey superProjectBranch)
specifier|public
name|SubmoduleSectionParser
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
annotation|@
name|Assisted
name|BlobBasedConfig
name|bbc
parameter_list|,
annotation|@
name|Assisted
name|String
name|thisServer
parameter_list|,
annotation|@
name|Assisted
name|Branch
operator|.
name|NameKey
name|superProjectBranch
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|bbc
operator|=
name|bbc
expr_stmt|;
name|this
operator|.
name|thisServer
operator|=
name|thisServer
expr_stmt|;
name|this
operator|.
name|superProjectBranch
operator|=
name|superProjectBranch
expr_stmt|;
block|}
DECL|method|parseAllSections ()
specifier|public
name|List
argument_list|<
name|SubmoduleSubscription
argument_list|>
name|parseAllSections
parameter_list|()
block|{
name|List
argument_list|<
name|SubmoduleSubscription
argument_list|>
name|parsedSubscriptions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|String
name|id
range|:
name|bbc
operator|.
name|getSubsections
argument_list|(
literal|"submodule"
argument_list|)
control|)
block|{
specifier|final
name|SubmoduleSubscription
name|subscription
init|=
name|parse
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|subscription
operator|!=
literal|null
condition|)
block|{
name|parsedSubscriptions
operator|.
name|add
argument_list|(
name|subscription
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|parsedSubscriptions
return|;
block|}
DECL|method|parse (final String id)
specifier|private
name|SubmoduleSubscription
name|parse
parameter_list|(
specifier|final
name|String
name|id
parameter_list|)
block|{
specifier|final
name|String
name|url
init|=
name|bbc
operator|.
name|getString
argument_list|(
literal|"submodule"
argument_list|,
name|id
argument_list|,
literal|"url"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|path
init|=
name|bbc
operator|.
name|getString
argument_list|(
literal|"submodule"
argument_list|,
name|id
argument_list|,
literal|"path"
argument_list|)
decl_stmt|;
name|String
name|branch
init|=
name|bbc
operator|.
name|getString
argument_list|(
literal|"submodule"
argument_list|,
name|id
argument_list|,
literal|"branch"
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|url
operator|!=
literal|null
operator|&&
name|url
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|&&
name|path
operator|!=
literal|null
operator|&&
name|path
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|&&
name|branch
operator|!=
literal|null
operator|&&
name|branch
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// All required fields filled.
name|boolean
name|urlIsRelative
init|=
name|url
operator|.
name|startsWith
argument_list|(
literal|"../"
argument_list|)
decl_stmt|;
name|String
name|server
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|urlIsRelative
condition|)
block|{
comment|// It is actually an URI. It could be ssh://localhost/project-a.
name|server
operator|=
operator|new
name|URI
argument_list|(
name|url
argument_list|)
operator|.
name|getHost
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|urlIsRelative
operator|)
operator|||
operator|(
name|server
operator|!=
literal|null
operator|&&
name|server
operator|.
name|equalsIgnoreCase
argument_list|(
name|thisServer
argument_list|)
operator|)
condition|)
block|{
comment|// Subscription really related to this running server.
if|if
condition|(
name|branch
operator|.
name|equals
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|branch
operator|=
name|superProjectBranch
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|branch
operator|.
name|startsWith
argument_list|(
name|Constants
operator|.
name|R_REFS
argument_list|)
condition|)
block|{
name|branch
operator|=
name|Constants
operator|.
name|R_HEADS
operator|+
name|branch
expr_stmt|;
block|}
specifier|final
name|String
name|urlExtractedPath
init|=
operator|new
name|URI
argument_list|(
name|url
argument_list|)
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|String
name|projectName
decl_stmt|;
name|int
name|fromIndex
init|=
name|urlExtractedPath
operator|.
name|length
argument_list|()
operator|-
literal|1
decl_stmt|;
while|while
condition|(
name|fromIndex
operator|>
literal|0
condition|)
block|{
name|fromIndex
operator|=
name|urlExtractedPath
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|,
name|fromIndex
operator|-
literal|1
argument_list|)
expr_stmt|;
name|projectName
operator|=
name|urlExtractedPath
operator|.
name|substring
argument_list|(
name|fromIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|projectName
operator|.
name|endsWith
argument_list|(
name|Constants
operator|.
name|DOT_GIT_EXT
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
comment|//
name|projectName
operator|.
name|length
argument_list|()
operator|-
name|Constants
operator|.
name|DOT_GIT_EXT
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|repoManager
operator|.
name|list
argument_list|()
operator|.
name|contains
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|projectName
argument_list|)
argument_list|)
condition|)
block|{
return|return
operator|new
name|SubmoduleSubscription
argument_list|(
name|superProjectBranch
argument_list|,
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|projectName
argument_list|)
argument_list|,
name|branch
argument_list|)
argument_list|,
name|path
argument_list|)
return|;
block|}
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|// Error in url syntax (in fact it is uri syntax)
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

