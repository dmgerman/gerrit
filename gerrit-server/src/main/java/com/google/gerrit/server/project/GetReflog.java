begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
name|common
operator|.
name|base
operator|.
name|Function
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
name|collect
operator|.
name|Lists
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
name|common
operator|.
name|GitPerson
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
name|extensions
operator|.
name|restapi
operator|.
name|ResourceNotFoundException
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
name|RestReadView
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
name|CommonConverters
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
name|args4j
operator|.
name|TimestampHandler
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|RepositoryNotFoundException
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
name|ReflogEntry
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
name|ReflogReader
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
name|Repository
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
name|Option
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
name|sql
operator|.
name|Timestamp
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

begin_class
DECL|class|GetReflog
specifier|public
class|class
name|GetReflog
implements|implements
name|RestReadView
argument_list|<
name|BranchResource
argument_list|>
block|{
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--limit"
argument_list|,
name|aliases
operator|=
block|{
literal|"-n"
block|}
argument_list|,
name|metaVar
operator|=
literal|"CNT"
argument_list|,
name|usage
operator|=
literal|"maximum number of reflog entries to list"
argument_list|)
DECL|method|setLimit (int limit)
specifier|public
name|GetReflog
name|setLimit
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--from"
argument_list|,
name|metaVar
operator|=
literal|"TIMESTAMP"
argument_list|,
name|usage
operator|=
literal|"timestamp from which the reflog entries should be listed (UTC, format: "
operator|+
name|TimestampHandler
operator|.
name|TIMESTAMP_FORMAT
operator|+
literal|")"
argument_list|)
DECL|method|setFrom (Timestamp from)
specifier|public
name|GetReflog
name|setFrom
parameter_list|(
name|Timestamp
name|from
parameter_list|)
block|{
name|this
operator|.
name|from
operator|=
name|from
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--to"
argument_list|,
name|metaVar
operator|=
literal|"TIMESTAMP"
argument_list|,
name|usage
operator|=
literal|"timestamp until which the reflog entries should be listed (UTC, format: "
operator|+
name|TimestampHandler
operator|.
name|TIMESTAMP_FORMAT
operator|+
literal|")"
argument_list|)
DECL|method|setTo (Timestamp to)
specifier|public
name|GetReflog
name|setTo
parameter_list|(
name|Timestamp
name|to
parameter_list|)
block|{
name|this
operator|.
name|to
operator|=
name|to
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|field|limit
specifier|private
name|int
name|limit
decl_stmt|;
DECL|field|from
specifier|private
name|Timestamp
name|from
decl_stmt|;
DECL|field|to
specifier|private
name|Timestamp
name|to
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetReflog (GitRepositoryManager repoManager)
specifier|public
name|GetReflog
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (BranchResource rsrc)
specifier|public
name|List
argument_list|<
name|ReflogEntryInfo
argument_list|>
name|apply
parameter_list|(
name|BranchResource
name|rsrc
parameter_list|)
throws|throws
name|AuthException
throws|,
name|ResourceNotFoundException
throws|,
name|RepositoryNotFoundException
throws|,
name|IOException
block|{
if|if
condition|(
operator|!
name|rsrc
operator|.
name|getControl
argument_list|()
operator|.
name|isOwner
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"not project owner"
argument_list|)
throw|;
block|}
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|rsrc
operator|.
name|getNameKey
argument_list|()
argument_list|)
init|)
block|{
name|ReflogReader
name|r
init|=
name|repo
operator|.
name|getReflogReader
argument_list|(
name|rsrc
operator|.
name|getRef
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|rsrc
operator|.
name|getRef
argument_list|()
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|ReflogEntry
argument_list|>
name|entries
decl_stmt|;
if|if
condition|(
name|from
operator|==
literal|null
operator|&&
name|to
operator|==
literal|null
condition|)
block|{
name|entries
operator|=
name|limit
operator|>
literal|0
condition|?
name|r
operator|.
name|getReverseEntries
argument_list|(
name|limit
argument_list|)
else|:
name|r
operator|.
name|getReverseEntries
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|entries
operator|=
name|limit
operator|>
literal|0
condition|?
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|limit
argument_list|)
else|:
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|ReflogEntry
name|e
range|:
name|r
operator|.
name|getReverseEntries
argument_list|()
control|)
block|{
name|Timestamp
name|timestamp
init|=
operator|new
name|Timestamp
argument_list|(
name|e
operator|.
name|getWho
argument_list|()
operator|.
name|getWhen
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|from
operator|==
literal|null
operator|||
name|from
operator|.
name|before
argument_list|(
name|timestamp
argument_list|)
operator|)
operator|&&
operator|(
name|to
operator|==
literal|null
operator|||
name|to
operator|.
name|after
argument_list|(
name|timestamp
argument_list|)
operator|)
condition|)
block|{
name|entries
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|limit
operator|>
literal|0
operator|&&
name|entries
operator|.
name|size
argument_list|()
operator|>=
name|limit
condition|)
block|{
break|break;
block|}
block|}
block|}
return|return
name|Lists
operator|.
name|transform
argument_list|(
name|entries
argument_list|,
operator|new
name|Function
argument_list|<
name|ReflogEntry
argument_list|,
name|ReflogEntryInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ReflogEntryInfo
name|apply
parameter_list|(
name|ReflogEntry
name|e
parameter_list|)
block|{
return|return
operator|new
name|ReflogEntryInfo
argument_list|(
name|e
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
block|}
DECL|class|ReflogEntryInfo
specifier|public
specifier|static
class|class
name|ReflogEntryInfo
block|{
DECL|field|oldId
specifier|public
name|String
name|oldId
decl_stmt|;
DECL|field|newId
specifier|public
name|String
name|newId
decl_stmt|;
DECL|field|who
specifier|public
name|GitPerson
name|who
decl_stmt|;
DECL|field|comment
specifier|public
name|String
name|comment
decl_stmt|;
DECL|method|ReflogEntryInfo (ReflogEntry e)
specifier|public
name|ReflogEntryInfo
parameter_list|(
name|ReflogEntry
name|e
parameter_list|)
block|{
name|oldId
operator|=
name|e
operator|.
name|getOldId
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
name|newId
operator|=
name|e
operator|.
name|getNewId
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
name|who
operator|=
name|CommonConverters
operator|.
name|toGitPerson
argument_list|(
name|e
operator|.
name|getWho
argument_list|()
argument_list|)
expr_stmt|;
name|comment
operator|=
name|e
operator|.
name|getComment
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

