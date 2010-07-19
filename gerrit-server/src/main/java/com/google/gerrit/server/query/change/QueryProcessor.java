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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
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
name|reviewdb
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
name|events
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
name|events
operator|.
name|QueryStats
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
name|Predicate
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
name|client
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
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
name|text
operator|.
name|SimpleDateFormat
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
DECL|class|QueryProcessor
specifier|public
class|class
name|QueryProcessor
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
name|QueryProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|enum|OutputFormat
specifier|public
specifier|static
enum|enum
name|OutputFormat
block|{
DECL|enumConstant|TEXT
DECL|enumConstant|JSON
name|TEXT
block|,
name|JSON
block|;   }
DECL|field|gson
specifier|private
specifier|final
name|Gson
name|gson
init|=
operator|new
name|Gson
argument_list|()
decl_stmt|;
DECL|field|sdf
specifier|private
specifier|final
name|SimpleDateFormat
name|sdf
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd HH:mm:ss zzz"
argument_list|)
decl_stmt|;
DECL|field|eventFactory
specifier|private
specifier|final
name|EventFactory
name|eventFactory
decl_stmt|;
DECL|field|queryBuilder
specifier|private
specifier|final
name|ChangeQueryBuilder
name|queryBuilder
decl_stmt|;
DECL|field|queryRewriter
specifier|private
specifier|final
name|ChangeQueryRewriter
name|queryRewriter
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|defaultLimit
specifier|private
name|int
name|defaultLimit
init|=
literal|500
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
DECL|method|QueryProcessor (EventFactory eventFactory, ChangeQueryBuilder.Factory queryBuilder, CurrentUser currentUser, ChangeQueryRewriter queryRewriter, Provider<ReviewDb> db)
name|QueryProcessor
parameter_list|(
name|EventFactory
name|eventFactory
parameter_list|,
name|ChangeQueryBuilder
operator|.
name|Factory
name|queryBuilder
parameter_list|,
name|CurrentUser
name|currentUser
parameter_list|,
name|ChangeQueryRewriter
name|queryRewriter
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|)
block|{
name|this
operator|.
name|eventFactory
operator|=
name|eventFactory
expr_stmt|;
name|this
operator|.
name|queryBuilder
operator|=
name|queryBuilder
operator|.
name|create
argument_list|(
name|currentUser
argument_list|)
expr_stmt|;
name|this
operator|.
name|queryRewriter
operator|=
name|queryRewriter
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
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
literal|"UTF-8"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
try|try
block|{
specifier|final
name|QueryStats
name|stats
init|=
operator|new
name|QueryStats
argument_list|()
decl_stmt|;
name|stats
operator|.
name|runTimeMilliseconds
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
specifier|final
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|visibleToMe
init|=
name|queryBuilder
operator|.
name|is_visible
argument_list|()
decl_stmt|;
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|s
init|=
name|compileQuery
argument_list|(
name|queryString
argument_list|,
name|visibleToMe
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ChangeData
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<
name|ChangeData
argument_list|>
argument_list|()
decl_stmt|;
name|HashSet
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|want
init|=
operator|new
name|HashSet
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ChangeData
name|d
range|:
operator|(
operator|(
name|ChangeDataSource
operator|)
name|s
operator|)
operator|.
name|read
argument_list|()
control|)
block|{
if|if
condition|(
name|d
operator|.
name|hasChange
argument_list|()
condition|)
block|{
comment|// Checking visibleToMe here should be unnecessary, the
comment|// query should have already performed it. But we don't
comment|// want to trust the query rewriter that much yet.
comment|//
if|if
condition|(
name|visibleToMe
operator|.
name|match
argument_list|(
name|d
argument_list|)
condition|)
block|{
name|results
operator|.
name|add
argument_list|(
name|d
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|want
operator|.
name|add
argument_list|(
name|d
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|want
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Change
name|c
range|:
name|db
operator|.
name|get
argument_list|()
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|want
argument_list|)
control|)
block|{
name|ChangeData
name|d
init|=
operator|new
name|ChangeData
argument_list|(
name|c
argument_list|)
decl_stmt|;
if|if
condition|(
name|visibleToMe
operator|.
name|match
argument_list|(
name|d
argument_list|)
condition|)
block|{
name|results
operator|.
name|add
argument_list|(
name|d
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|results
argument_list|,
operator|new
name|Comparator
argument_list|<
name|ChangeData
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|ChangeData
name|a
parameter_list|,
name|ChangeData
name|b
parameter_list|)
block|{
return|return
name|b
operator|.
name|getChange
argument_list|()
operator|.
name|getSortKey
argument_list|()
operator|.
name|compareTo
argument_list|(
name|a
operator|.
name|getChange
argument_list|()
operator|.
name|getSortKey
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|defaultLimit
operator|<
name|results
operator|.
name|size
argument_list|()
condition|)
block|{
name|results
operator|=
name|results
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|defaultLimit
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ChangeData
name|d
range|:
name|results
control|)
block|{
name|ChangeAttribute
name|c
init|=
name|eventFactory
operator|.
name|asChangeAttribute
argument_list|(
name|d
operator|.
name|getChange
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
name|getChange
argument_list|()
argument_list|)
expr_stmt|;
name|eventFactory
operator|.
name|addTrackingIds
argument_list|(
name|c
argument_list|,
name|d
operator|.
name|trackingIds
argument_list|(
name|db
argument_list|)
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
name|c
argument_list|,
name|d
operator|.
name|patches
argument_list|(
name|db
argument_list|)
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
argument_list|(
name|db
argument_list|)
decl_stmt|;
if|if
condition|(
name|current
operator|!=
literal|null
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
comment|//
name|d
operator|.
name|approvalsFor
argument_list|(
name|db
argument_list|,
name|current
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|show
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
name|stats
operator|.
name|rowCount
operator|=
name|results
operator|.
name|size
argument_list|()
expr_stmt|;
name|stats
operator|.
name|runTimeMilliseconds
operator|=
name|System
operator|.
name|currentTimeMillis
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|compileQuery (String queryString, final Predicate<ChangeData> visibleToMe)
specifier|private
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|compileQuery
parameter_list|(
name|String
name|queryString
parameter_list|,
specifier|final
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|visibleToMe
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|q
init|=
name|queryBuilder
operator|.
name|parse
argument_list|(
name|queryString
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|queryBuilder
operator|.
name|hasLimit
argument_list|(
name|q
argument_list|)
condition|)
block|{
name|q
operator|=
name|Predicate
operator|.
name|and
argument_list|(
name|q
argument_list|,
name|queryBuilder
operator|.
name|limit
argument_list|(
name|defaultLimit
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|queryBuilder
operator|.
name|hasSortKey
argument_list|(
name|q
argument_list|)
condition|)
block|{
name|q
operator|=
name|Predicate
operator|.
name|and
argument_list|(
name|q
argument_list|,
name|queryBuilder
operator|.
name|sortkey_before
argument_list|(
literal|"z"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|q
operator|=
name|Predicate
operator|.
name|and
argument_list|(
name|q
argument_list|,
name|visibleToMe
argument_list|)
expr_stmt|;
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|s
init|=
name|queryRewriter
operator|.
name|rewrite
argument_list|(
name|q
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|s
operator|instanceof
name|ChangeDataSource
operator|)
condition|)
block|{
name|s
operator|=
name|queryRewriter
operator|.
name|rewrite
argument_list|(
name|Predicate
operator|.
name|and
argument_list|(
name|queryBuilder
operator|.
name|status_open
argument_list|()
argument_list|,
name|q
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
operator|(
name|s
operator|instanceof
name|ChangeDataSource
operator|)
condition|)
block|{
throw|throw
operator|new
name|QueryParseException
argument_list|(
literal|"cannot execute query: "
operator|+
name|s
argument_list|)
throw|;
block|}
return|return
name|s
return|;
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
name|gson
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
name|indent
argument_list|(
name|depth
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|f
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
if|if
condition|(
name|val
operator|instanceof
name|Long
operator|&&
name|isDateField
argument_list|(
name|f
operator|.
name|getName
argument_list|()
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
name|sdf
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|(
operator|(
operator|(
name|Long
operator|)
name|val
operator|)
operator|*
literal|1000L
argument_list|)
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
else|else
block|{
name|showTextValue
argument_list|(
name|val
argument_list|,
name|depth
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|indent (int depth)
specifier|private
name|void
name|indent
parameter_list|(
name|int
name|depth
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|depth
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"  "
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"cast"
block|,
literal|"unchecked"
block|}
argument_list|)
DECL|method|showTextValue (Object value, int depth)
specifier|private
name|void
name|showTextValue
parameter_list|(
name|Object
name|value
parameter_list|,
name|int
name|depth
parameter_list|)
block|{
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
for|for
control|(
name|Object
name|thing
range|:
operator|(
operator|(
name|Collection
operator|)
name|value
operator|)
control|)
block|{
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
name|out
operator|.
name|print
argument_list|(
literal|'\n'
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
argument_list|<
name|Field
argument_list|>
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

