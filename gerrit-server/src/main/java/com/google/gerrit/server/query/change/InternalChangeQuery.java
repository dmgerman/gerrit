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
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|Predicate
operator|.
name|and
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
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/** Execute a single query over changes, for use by Gerrit internals. */
end_comment

begin_class
DECL|class|InternalChangeQuery
specifier|public
class|class
name|InternalChangeQuery
block|{
DECL|field|qp
specifier|private
specifier|final
name|QueryProcessor
name|qp
decl_stmt|;
DECL|field|qb
specifier|private
specifier|final
name|ChangeQueryBuilder
name|qb
decl_stmt|;
annotation|@
name|Inject
DECL|method|InternalChangeQuery (QueryProcessor queryProcessor, ChangeQueryBuilder queryBuilder)
name|InternalChangeQuery
parameter_list|(
name|QueryProcessor
name|queryProcessor
parameter_list|,
name|ChangeQueryBuilder
name|queryBuilder
parameter_list|)
block|{
name|qp
operator|=
name|queryProcessor
expr_stmt|;
name|qb
operator|=
name|queryBuilder
expr_stmt|;
block|}
DECL|method|setLimit (int n)
specifier|public
name|InternalChangeQuery
name|setLimit
parameter_list|(
name|int
name|n
parameter_list|)
block|{
name|qp
operator|.
name|setLimit
argument_list|(
name|n
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|byProjectOpen (Project.NameKey project)
specifier|public
name|List
argument_list|<
name|ChangeData
argument_list|>
name|byProjectOpen
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|query
argument_list|(
name|and
argument_list|(
name|qb
operator|.
name|project
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
name|qb
operator|.
name|status_open
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|query (Predicate<ChangeData> p)
specifier|private
name|List
argument_list|<
name|ChangeData
argument_list|>
name|query
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
parameter_list|)
throws|throws
name|OrmException
block|{
try|try
block|{
return|return
name|qp
operator|.
name|queryChanges
argument_list|(
name|p
argument_list|)
operator|.
name|changes
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|QueryParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

