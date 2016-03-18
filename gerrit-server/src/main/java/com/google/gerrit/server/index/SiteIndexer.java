begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
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
name|checkNotNull
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
name|base
operator|.
name|Stopwatch
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
name|NullOutputStream
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
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_class
DECL|class|SiteIndexer
specifier|public
specifier|abstract
class|class
name|SiteIndexer
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|,
name|I
extends|extends
name|Index
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
parameter_list|>
block|{
DECL|class|Result
specifier|public
specifier|static
class|class
name|Result
block|{
DECL|field|elapsedNanos
specifier|private
specifier|final
name|long
name|elapsedNanos
decl_stmt|;
DECL|field|success
specifier|private
specifier|final
name|boolean
name|success
decl_stmt|;
DECL|field|done
specifier|private
specifier|final
name|int
name|done
decl_stmt|;
DECL|field|failed
specifier|private
specifier|final
name|int
name|failed
decl_stmt|;
DECL|method|Result (Stopwatch sw, boolean success, int done, int failed)
specifier|public
name|Result
parameter_list|(
name|Stopwatch
name|sw
parameter_list|,
name|boolean
name|success
parameter_list|,
name|int
name|done
parameter_list|,
name|int
name|failed
parameter_list|)
block|{
name|this
operator|.
name|elapsedNanos
operator|=
name|sw
operator|.
name|elapsed
argument_list|(
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
expr_stmt|;
name|this
operator|.
name|success
operator|=
name|success
expr_stmt|;
name|this
operator|.
name|done
operator|=
name|done
expr_stmt|;
name|this
operator|.
name|failed
operator|=
name|failed
expr_stmt|;
block|}
DECL|method|success ()
specifier|public
name|boolean
name|success
parameter_list|()
block|{
return|return
name|success
return|;
block|}
DECL|method|doneCount ()
specifier|public
name|int
name|doneCount
parameter_list|()
block|{
return|return
name|done
return|;
block|}
DECL|method|failedCount ()
specifier|public
name|int
name|failedCount
parameter_list|()
block|{
return|return
name|failed
return|;
block|}
DECL|method|elapsed (TimeUnit timeUnit)
specifier|public
name|long
name|elapsed
parameter_list|(
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
return|return
name|timeUnit
operator|.
name|convert
argument_list|(
name|elapsedNanos
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
return|;
block|}
block|}
DECL|field|totalWork
specifier|protected
name|int
name|totalWork
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|progressOut
specifier|protected
name|OutputStream
name|progressOut
init|=
name|NullOutputStream
operator|.
name|INSTANCE
decl_stmt|;
DECL|field|verboseWriter
specifier|protected
name|PrintWriter
name|verboseWriter
init|=
operator|new
name|PrintWriter
argument_list|(
name|NullOutputStream
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
DECL|method|setTotalWork (int num)
specifier|public
name|void
name|setTotalWork
parameter_list|(
name|int
name|num
parameter_list|)
block|{
name|totalWork
operator|=
name|num
expr_stmt|;
block|}
DECL|method|setProgressOut (OutputStream out)
specifier|public
name|void
name|setProgressOut
parameter_list|(
name|OutputStream
name|out
parameter_list|)
block|{
name|progressOut
operator|=
name|checkNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|setVerboseOut (OutputStream out)
specifier|public
name|void
name|setVerboseOut
parameter_list|(
name|OutputStream
name|out
parameter_list|)
block|{
name|verboseWriter
operator|=
operator|new
name|PrintWriter
argument_list|(
name|checkNotNull
argument_list|(
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|indexAll (I index)
specifier|public
specifier|abstract
name|Result
name|indexAll
parameter_list|(
name|I
name|index
parameter_list|)
function_decl|;
block|}
end_class

end_unit

