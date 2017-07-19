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
DECL|package|com.google.gerrit.server.query.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkState
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
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
name|common
operator|.
name|TimeUtil
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
name|common
operator|.
name|data
operator|.
name|LabelTypes
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
name|PatchSet
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
name|server
operator|.
name|ReviewDb
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
name|gerrit
operator|.
name|server
operator|.
name|account
operator|.
name|AccountCache
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
name|server
operator|.
name|config
operator|.
name|TrackingFooters
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
name|PatchSetAttribute
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
name|QueryStatsAttribute
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
name|events
operator|.
name|EventFactory
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
name|gerrit
operator|.
name|server
operator|.
name|project
operator|.
name|ChangeControl
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
name|SubmitRuleEvaluator
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
name|query
operator|.
name|QueryParseException
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
name|query
operator|.
name|QueryResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|Gson
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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
name|java
operator|.
name|io
operator|.
name|BufferedWriter
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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|List
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
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|RevWalk
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
name|util
operator|.
name|io
operator|.
name|DisabledOutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|format
operator|.
name|DateTimeFormat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|format
operator|.
name|DateTimeFormatter
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

begin_comment
comment|/** Change query implementation that outputs to a stream in the style of an SSH command. */
end_comment

begin_class
DECL|class|OutputStreamQuery
specifier|public
class|class
name|OutputStreamQuery
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
name|OutputStreamQuery
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|dtf
specifier|private
specifier|static
specifier|final
name|DateTimeFormatter
name|dtf
init|=
name|DateTimeFormat
operator|.
name|forPattern
argument_list|(
literal|"yyyy-MM-dd HH:mm:ss zzz"
argument_list|)
decl_stmt|;
DECL|enum|OutputFormat
specifier|public
enum|enum
name|OutputFormat
block|{
DECL|enumConstant|TEXT
name|TEXT
block|,
DECL|enumConstant|JSON
name|JSON
block|}
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|accounts
specifier|private
specifier|final
name|Accounts
name|accounts
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|queryBuilder
specifier|private
specifier|final
name|ChangeQueryBuilder
name|queryBuilder
decl_stmt|;
DECL|field|queryProcessor
specifier|private
specifier|final
name|ChangeQueryProcessor
name|queryProcessor
decl_stmt|;
DECL|field|eventFactory
specifier|private
specifier|final
name|EventFactory
name|eventFactory
decl_stmt|;
DECL|field|trackingFooters
specifier|private
specifier|final
name|TrackingFooters
name|trackingFooters
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|CurrentUser
name|user
decl_stmt|;
DECL|field|outputFormat
specifier|private
name|OutputFormat
name|outputFormat
init|=
name|OutputFormat
operator|.
name|TEXT
decl_stmt|;
DECL|field|includePatchSets
specifier|private
name|boolean
name|includePatchSets
decl_stmt|;
DECL|field|includeCurrentPatchSet
specifier|private
name|boolean
name|includeCurrentPatchSet
decl_stmt|;
DECL|field|includeApprovals
specifier|private
name|boolean
name|includeApprovals
decl_stmt|;
DECL|field|includeComments
specifier|private
name|boolean
name|includeComments
decl_stmt|;
DECL|field|includeFiles
specifier|private
name|boolean
name|includeFiles
decl_stmt|;
DECL|field|includeCommitMessage
specifier|private
name|boolean
name|includeCommitMessage
decl_stmt|;
DECL|field|includeDependencies
specifier|private
name|boolean
name|includeDependencies
decl_stmt|;
DECL|field|includeSubmitRecords
specifier|private
name|boolean
name|includeSubmitRecords
decl_stmt|;
DECL|field|includeAllReviewers
specifier|private
name|boolean
name|includeAllReviewers
decl_stmt|;
DECL|field|outputStream
specifier|private
name|OutputStream
name|outputStream
init|=
name|DisabledOutputStream
operator|.
name|INSTANCE
decl_stmt|;
DECL|field|out
specifier|private
name|PrintWriter
name|out
decl_stmt|;
annotation|@
name|Inject
DECL|method|OutputStreamQuery ( ReviewDb db, AccountCache accountCache, Accounts accounts, GitRepositoryManager repoManager, ChangeQueryBuilder queryBuilder, ChangeQueryProcessor queryProcessor, EventFactory eventFactory, TrackingFooters trackingFooters, CurrentUser user)
name|OutputStreamQuery
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|Accounts
name|accounts
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|ChangeQueryBuilder
name|queryBuilder
parameter_list|,
name|ChangeQueryProcessor
name|queryProcessor
parameter_list|,
name|EventFactory
name|eventFactory
parameter_list|,
name|TrackingFooters
name|trackingFooters
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
block|{
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|accounts
operator|=
name|accounts
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|queryBuilder
operator|=
name|queryBuilder
expr_stmt|;
name|this
operator|.
name|queryProcessor
operator|=
name|queryProcessor
expr_stmt|;
name|this
operator|.
name|eventFactory
operator|=
name|eventFactory
expr_stmt|;
name|this
operator|.
name|trackingFooters
operator|=
name|trackingFooters
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
DECL|method|setLimit (int n)
name|void
name|setLimit
parameter_list|(
name|int
name|n
parameter_list|)
block|{
name|queryProcessor
operator|.
name|setLimit
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
DECL|method|setStart (int n)
specifier|public
name|void
name|setStart
parameter_list|(
name|int
name|n
parameter_list|)
block|{
name|queryProcessor
operator|.
name|setStart
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
DECL|method|setIncludePatchSets (boolean on)
specifier|public
name|void
name|setIncludePatchSets
parameter_list|(
name|boolean
name|on
parameter_list|)
block|{
name|includePatchSets
operator|=
name|on
expr_stmt|;
block|}
DECL|method|getIncludePatchSets ()
specifier|public
name|boolean
name|getIncludePatchSets
parameter_list|()
block|{
return|return
name|includePatchSets
return|;
block|}
DECL|method|setIncludeCurrentPatchSet (boolean on)
specifier|public
name|void
name|setIncludeCurrentPatchSet
parameter_list|(
name|boolean
name|on
parameter_list|)
block|{
name|includeCurrentPatchSet
operator|=
name|on
expr_stmt|;
block|}
DECL|method|getIncludeCurrentPatchSet ()
specifier|public
name|boolean
name|getIncludeCurrentPatchSet
parameter_list|()
block|{
return|return
name|includeCurrentPatchSet
return|;
block|}
DECL|method|setIncludeApprovals (boolean on)
specifier|public
name|void
name|setIncludeApprovals
parameter_list|(
name|boolean
name|on
parameter_list|)
block|{
name|includeApprovals
operator|=
name|on
expr_stmt|;
block|}
DECL|method|setIncludeComments (boolean on)
specifier|public
name|void
name|setIncludeComments
parameter_list|(
name|boolean
name|on
parameter_list|)
block|{
name|includeComments
operator|=
name|on
expr_stmt|;
block|}
DECL|method|setIncludeFiles (boolean on)
specifier|public
name|void
name|setIncludeFiles
parameter_list|(
name|boolean
name|on
parameter_list|)
block|{
name|includeFiles
operator|=
name|on
expr_stmt|;
block|}
DECL|method|getIncludeFiles ()
specifier|public
name|boolean
name|getIncludeFiles
parameter_list|()
block|{
return|return
name|includeFiles
return|;
block|}
DECL|method|setIncludeDependencies (boolean on)
specifier|public
name|void
name|setIncludeDependencies
parameter_list|(
name|boolean
name|on
parameter_list|)
block|{
name|includeDependencies
operator|=
name|on
expr_stmt|;
block|}
DECL|method|getIncludeDependencies ()
specifier|public
name|boolean
name|getIncludeDependencies
parameter_list|()
block|{
return|return
name|includeDependencies
return|;
block|}
DECL|method|setIncludeCommitMessage (boolean on)
specifier|public
name|void
name|setIncludeCommitMessage
parameter_list|(
name|boolean
name|on
parameter_list|)
block|{
name|includeCommitMessage
operator|=
name|on
expr_stmt|;
block|}
DECL|method|setIncludeSubmitRecords (boolean on)
specifier|public
name|void
name|setIncludeSubmitRecords
parameter_list|(
name|boolean
name|on
parameter_list|)
block|{
name|includeSubmitRecords
operator|=
name|on
expr_stmt|;
block|}
DECL|method|setIncludeAllReviewers (boolean on)
specifier|public
name|void
name|setIncludeAllReviewers
parameter_list|(
name|boolean
name|on
parameter_list|)
block|{
name|includeAllReviewers
operator|=
name|on
expr_stmt|;
block|}
DECL|method|setOutput (OutputStream out, OutputFormat fmt)
specifier|public
name|void
name|setOutput
parameter_list|(
name|OutputStream
name|out
parameter_list|,
name|OutputFormat
name|fmt
parameter_list|)
block|{
name|this
operator|.
name|outputStream
operator|=
name|out
expr_stmt|;
name|this
operator|.
name|outputFormat
operator|=
name|fmt
expr_stmt|;
block|}
DECL|method|query (String queryString)
specifier|public
name|void
name|query
parameter_list|(
name|String
name|queryString
parameter_list|)
throws|throws
name|IOException
block|{
name|out
operator|=
operator|new
name|PrintWriter
argument_list|(
comment|//
operator|new
name|BufferedWriter
argument_list|(
comment|//
operator|new
name|OutputStreamWriter
argument_list|(
name|outputStream
argument_list|,
name|UTF_8
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|queryProcessor
operator|.
name|isDisabled
argument_list|()
condition|)
block|{
name|ErrorMessage
name|m
init|=
operator|new
name|ErrorMessage
argument_list|()
decl_stmt|;
name|m
operator|.
name|message
operator|=
literal|"query disabled"
expr_stmt|;
name|show
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return;
block|}
try|try
block|{
specifier|final
name|QueryStatsAttribute
name|stats
init|=
operator|new
name|QueryStatsAttribute
argument_list|()
decl_stmt|;
name|stats
operator|.
name|runTimeMilliseconds
operator|=
name|TimeUtil
operator|.
name|nowMs
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|Repository
argument_list|>
name|repos
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|RevWalk
argument_list|>
name|revWalks
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|QueryResult
argument_list|<
name|ChangeData
argument_list|>
name|results
init|=
name|queryProcessor
operator|.
name|query
argument_list|(
name|queryBuilder
operator|.
name|parse
argument_list|(
name|queryString
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
for|for
control|(
name|ChangeData
name|d
range|:
name|results
operator|.
name|entities
argument_list|()
control|)
block|{
name|show
argument_list|(
name|buildChangeAttribute
argument_list|(
name|d
argument_list|,
name|repos
argument_list|,
name|revWalks
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|closeAll
argument_list|(
name|revWalks
operator|.
name|values
argument_list|()
argument_list|,
name|repos
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|stats
operator|.
name|rowCount
operator|=
name|results
operator|.
name|entities
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
name|stats
operator|.
name|moreChanges
operator|=
name|results
operator|.
name|more
argument_list|()
expr_stmt|;
name|stats
operator|.
name|runTimeMilliseconds
operator|=
name|TimeUtil
operator|.
name|nowMs
argument_list|()
operator|-
name|stats
operator|.
name|runTimeMilliseconds
expr_stmt|;
name|show
argument_list|(
name|stats
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot execute query: "
operator|+
name|queryString
argument_list|,
name|err
argument_list|)
expr_stmt|;
name|ErrorMessage
name|m
init|=
operator|new
name|ErrorMessage
argument_list|()
decl_stmt|;
name|m
operator|.
name|message
operator|=
literal|"cannot query database"
expr_stmt|;
name|show
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|QueryParseException
name|e
parameter_list|)
block|{
name|ErrorMessage
name|m
init|=
operator|new
name|ErrorMessage
argument_list|()
decl_stmt|;
name|m
operator|.
name|message
operator|=
name|e
operator|.
name|getMessage
argument_list|()
expr_stmt|;
name|show
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
try|try
block|{
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|out
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
DECL|method|buildChangeAttribute ( ChangeData d, Map<Project.NameKey, Repository> repos, Map<Project.NameKey, RevWalk> revWalks)
specifier|private
name|ChangeAttribute
name|buildChangeAttribute
parameter_list|(
name|ChangeData
name|d
parameter_list|,
name|Map
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|Repository
argument_list|>
name|repos
parameter_list|,
name|Map
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|RevWalk
argument_list|>
name|revWalks
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
block|{
name|ChangeControl
name|cc
init|=
name|d
operator|.
name|changeControl
argument_list|()
operator|.
name|forUser
argument_list|(
name|user
argument_list|)
decl_stmt|;
name|LabelTypes
name|labelTypes
init|=
name|cc
operator|.
name|getLabelTypes
argument_list|()
decl_stmt|;
name|ChangeAttribute
name|c
init|=
name|eventFactory
operator|.
name|asChangeAttribute
argument_list|(
name|db
argument_list|,
name|d
operator|.
name|change
argument_list|()
argument_list|)
decl_stmt|;
name|eventFactory
operator|.
name|extend
argument_list|(
name|c
argument_list|,
name|d
operator|.
name|change
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|trackingFooters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|eventFactory
operator|.
name|addTrackingIds
argument_list|(
name|c
argument_list|,
name|trackingFooters
operator|.
name|extract
argument_list|(
name|d
operator|.
name|commitFooters
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|includeAllReviewers
condition|)
block|{
name|eventFactory
operator|.
name|addAllReviewers
argument_list|(
name|db
argument_list|,
name|c
argument_list|,
name|d
operator|.
name|notes
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|includeSubmitRecords
condition|)
block|{
name|eventFactory
operator|.
name|addSubmitRecords
argument_list|(
name|c
argument_list|,
operator|new
name|SubmitRuleEvaluator
argument_list|(
name|accountCache
argument_list|,
name|accounts
argument_list|,
name|d
argument_list|)
operator|.
name|setAllowClosed
argument_list|(
literal|true
argument_list|)
operator|.
name|setAllowDraft
argument_list|(
literal|true
argument_list|)
operator|.
name|evaluate
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|includeCommitMessage
condition|)
block|{
name|eventFactory
operator|.
name|addCommitMessage
argument_list|(
name|c
argument_list|,
name|d
operator|.
name|commitMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|RevWalk
name|rw
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|includePatchSets
operator|||
name|includeCurrentPatchSet
operator|||
name|includeDependencies
condition|)
block|{
name|Project
operator|.
name|NameKey
name|p
init|=
name|d
operator|.
name|change
argument_list|()
operator|.
name|getProject
argument_list|()
decl_stmt|;
name|rw
operator|=
name|revWalks
operator|.
name|get
argument_list|(
name|p
argument_list|)
expr_stmt|;
comment|// Cache and reuse repos and revwalks.
if|if
condition|(
name|rw
operator|==
literal|null
condition|)
block|{
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|checkState
argument_list|(
name|repos
operator|.
name|put
argument_list|(
name|p
argument_list|,
name|repo
argument_list|)
operator|==
literal|null
argument_list|)
expr_stmt|;
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|revWalks
operator|.
name|put
argument_list|(
name|p
argument_list|,
name|rw
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|includePatchSets
condition|)
block|{
name|eventFactory
operator|.
name|addPatchSets
argument_list|(
name|db
argument_list|,
name|rw
argument_list|,
name|c
argument_list|,
name|d
operator|.
name|visiblePatchSets
argument_list|()
argument_list|,
name|includeApprovals
condition|?
name|d
operator|.
name|approvals
argument_list|()
operator|.
name|asMap
argument_list|()
else|:
literal|null
argument_list|,
name|includeFiles
argument_list|,
name|d
operator|.
name|change
argument_list|()
argument_list|,
name|labelTypes
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|includeCurrentPatchSet
condition|)
block|{
name|PatchSet
name|current
init|=
name|d
operator|.
name|currentPatchSet
argument_list|()
decl_stmt|;
if|if
condition|(
name|current
operator|!=
literal|null
operator|&&
name|cc
operator|.
name|isPatchVisible
argument_list|(
name|current
argument_list|,
name|d
operator|.
name|db
argument_list|()
argument_list|)
condition|)
block|{
name|c
operator|.
name|currentPatchSet
operator|=
name|eventFactory
operator|.
name|asPatchSetAttribute
argument_list|(
name|db
argument_list|,
name|rw
argument_list|,
name|d
operator|.
name|change
argument_list|()
argument_list|,
name|current
argument_list|)
expr_stmt|;
name|eventFactory
operator|.
name|addApprovals
argument_list|(
name|c
operator|.
name|currentPatchSet
argument_list|,
name|d
operator|.
name|currentApprovals
argument_list|()
argument_list|,
name|labelTypes
argument_list|)
expr_stmt|;
if|if
condition|(
name|includeFiles
condition|)
block|{
name|eventFactory
operator|.
name|addPatchSetFileNames
argument_list|(
name|c
operator|.
name|currentPatchSet
argument_list|,
name|d
operator|.
name|change
argument_list|()
argument_list|,
name|d
operator|.
name|currentPatchSet
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|includeComments
condition|)
block|{
name|eventFactory
operator|.
name|addPatchSetComments
argument_list|(
name|c
operator|.
name|currentPatchSet
argument_list|,
name|d
operator|.
name|publishedComments
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|includeComments
condition|)
block|{
name|eventFactory
operator|.
name|addComments
argument_list|(
name|c
argument_list|,
name|d
operator|.
name|messages
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|includePatchSets
condition|)
block|{
name|eventFactory
operator|.
name|addPatchSets
argument_list|(
name|db
argument_list|,
name|rw
argument_list|,
name|c
argument_list|,
name|d
operator|.
name|visiblePatchSets
argument_list|()
argument_list|,
name|includeApprovals
condition|?
name|d
operator|.
name|approvals
argument_list|()
operator|.
name|asMap
argument_list|()
else|:
literal|null
argument_list|,
name|includeFiles
argument_list|,
name|d
operator|.
name|change
argument_list|()
argument_list|,
name|labelTypes
argument_list|)
expr_stmt|;
for|for
control|(
name|PatchSetAttribute
name|attribute
range|:
name|c
operator|.
name|patchSets
control|)
block|{
name|eventFactory
operator|.
name|addPatchSetComments
argument_list|(
name|attribute
argument_list|,
name|d
operator|.
name|publishedComments
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|includeDependencies
condition|)
block|{
name|eventFactory
operator|.
name|addDependencies
argument_list|(
name|rw
argument_list|,
name|c
argument_list|,
name|d
operator|.
name|change
argument_list|()
argument_list|,
name|d
operator|.
name|currentPatchSet
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|c
operator|.
name|plugins
operator|=
name|queryProcessor
operator|.
name|create
argument_list|(
name|d
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
DECL|method|closeAll (Iterable<RevWalk> revWalks, Iterable<Repository> repos)
specifier|private
specifier|static
name|void
name|closeAll
parameter_list|(
name|Iterable
argument_list|<
name|RevWalk
argument_list|>
name|revWalks
parameter_list|,
name|Iterable
argument_list|<
name|Repository
argument_list|>
name|repos
parameter_list|)
block|{
if|if
condition|(
name|repos
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Repository
name|repo
range|:
name|repos
control|)
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|revWalks
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|RevWalk
name|revWalk
range|:
name|revWalks
control|)
block|{
name|revWalk
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|show (Object data)
specifier|private
name|void
name|show
parameter_list|(
name|Object
name|data
parameter_list|)
block|{
switch|switch
condition|(
name|outputFormat
condition|)
block|{
default|default:
case|case
name|TEXT
case|:
if|if
condition|(
name|data
operator|instanceof
name|ChangeAttribute
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"change "
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
operator|(
operator|(
name|ChangeAttribute
operator|)
name|data
operator|)
operator|.
name|id
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|showText
argument_list|(
name|data
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|showText
argument_list|(
name|data
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|print
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
break|break;
case|case
name|JSON
case|:
name|out
operator|.
name|print
argument_list|(
operator|new
name|Gson
argument_list|()
operator|.
name|toJson
argument_list|(
name|data
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
DECL|method|showText (Object data, int depth)
specifier|private
name|void
name|showText
parameter_list|(
name|Object
name|data
parameter_list|,
name|int
name|depth
parameter_list|)
block|{
for|for
control|(
name|Field
name|f
range|:
name|fieldsOf
argument_list|(
name|data
operator|.
name|getClass
argument_list|()
argument_list|)
control|)
block|{
name|Object
name|val
decl_stmt|;
try|try
block|{
name|val
operator|=
name|f
operator|.
name|get
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|err
parameter_list|)
block|{
continue|continue;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|err
parameter_list|)
block|{
continue|continue;
block|}
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|showField
argument_list|(
name|f
operator|.
name|getName
argument_list|()
argument_list|,
name|val
argument_list|,
name|depth
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|indent (int spaces)
specifier|private
name|String
name|indent
parameter_list|(
name|int
name|spaces
parameter_list|)
block|{
if|if
condition|(
name|spaces
operator|==
literal|0
condition|)
block|{
return|return
literal|""
return|;
block|}
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%"
operator|+
name|spaces
operator|+
literal|"s"
argument_list|,
literal|" "
argument_list|)
return|;
block|}
DECL|method|showField (String field, Object value, int depth)
specifier|private
name|void
name|showField
parameter_list|(
name|String
name|field
parameter_list|,
name|Object
name|value
parameter_list|,
name|int
name|depth
parameter_list|)
block|{
specifier|final
name|int
name|spacesDepthRatio
init|=
literal|2
decl_stmt|;
name|String
name|indent
init|=
name|indent
argument_list|(
name|depth
operator|*
name|spacesDepthRatio
argument_list|)
decl_stmt|;
name|out
operator|.
name|print
argument_list|(
name|indent
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|field
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|':'
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|String
operator|&&
operator|(
operator|(
name|String
operator|)
name|value
operator|)
operator|.
name|contains
argument_list|(
literal|"\n"
argument_list|)
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
comment|// Idention for multi-line text is
comment|// current depth indetion + length of field + length of ": "
name|indent
operator|=
name|indent
argument_list|(
name|indent
operator|.
name|length
argument_list|()
operator|+
name|field
operator|.
name|length
argument_list|()
operator|+
name|spacesDepthRatio
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
operator|(
operator|(
name|String
operator|)
name|value
operator|)
operator|.
name|replaceAll
argument_list|(
literal|"\n"
argument_list|,
literal|"\n"
operator|+
name|indent
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Long
operator|&&
name|isDateField
argument_list|(
name|field
argument_list|)
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|dtf
operator|.
name|print
argument_list|(
operator|(
operator|(
name|Long
operator|)
name|value
operator|)
operator|*
literal|1000L
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isPrimitive
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Collection
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|boolean
name|firstElement
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Object
name|thing
range|:
operator|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|value
operator|)
control|)
block|{
comment|// The name of the collection was initially printed at the beginning
comment|// of this routine.  Beginning at the second sub-element, reprint
comment|// the collection name so humans can separate individual elements
comment|// with less strain and error.
comment|//
if|if
condition|(
name|firstElement
condition|)
block|{
name|firstElement
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|print
argument_list|(
name|indent
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|field
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|":\n"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isPrimitive
argument_list|(
name|thing
argument_list|)
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|showText
argument_list|(
name|thing
argument_list|,
name|depth
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|out
operator|.
name|print
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|showText
argument_list|(
name|value
argument_list|,
name|depth
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isPrimitive (Object value)
specifier|private
specifier|static
name|boolean
name|isPrimitive
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|value
operator|instanceof
name|String
comment|//
operator|||
name|value
operator|instanceof
name|Number
comment|//
operator|||
name|value
operator|instanceof
name|Boolean
comment|//
operator|||
name|value
operator|instanceof
name|Enum
return|;
block|}
DECL|method|isDateField (String name)
specifier|private
specifier|static
name|boolean
name|isDateField
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|"lastUpdated"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
comment|//
operator|||
literal|"grantedOn"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
comment|//
operator|||
literal|"timestamp"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
comment|//
operator|||
literal|"createdOn"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|fieldsOf (Class<?> type)
specifier|private
name|List
argument_list|<
name|Field
argument_list|>
name|fieldsOf
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|List
argument_list|<
name|Field
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|getSuperclass
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|r
operator|.
name|addAll
argument_list|(
name|fieldsOf
argument_list|(
name|type
operator|.
name|getSuperclass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|r
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|type
operator|.
name|getDeclaredFields
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
DECL|class|ErrorMessage
specifier|static
class|class
name|ErrorMessage
block|{
DECL|field|type
specifier|public
specifier|final
name|String
name|type
init|=
literal|"error"
decl_stmt|;
DECL|field|message
specifier|public
name|String
name|message
decl_stmt|;
block|}
block|}
end_class

end_unit

