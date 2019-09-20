begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
operator|.
name|METHOD
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
operator|.
name|TYPE
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
operator|.
name|RUNTIME
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
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

begin_comment
comment|/**  * Annotation to use a clock step for the execution of acceptance tests (the test class must inherit  * from {@link AbstractDaemonTest}).  *  *<p>Annotations on method level override annotations on class level.  */
end_comment

begin_annotation_defn
annotation|@
name|Target
argument_list|(
block|{
name|TYPE
block|,
name|METHOD
block|}
argument_list|)
annotation|@
name|Retention
argument_list|(
name|RUNTIME
argument_list|)
DECL|annotation|UseClockStep
specifier|public
annotation_defn|@interface
name|UseClockStep
block|{
comment|/** Amount to increment clock by on each lookup. */
DECL|method|clockStep ()
DECL|field|L
name|long
name|clockStep
parameter_list|()
default|default
literal|1L
function_decl|;
comment|/** Time unit for {@link #clockStep()}. */
DECL|method|clockStepUnit ()
DECL|field|TimeUnit.SECONDS
name|TimeUnit
name|clockStepUnit
parameter_list|()
default|default
name|TimeUnit
operator|.
name|SECONDS
function_decl|;
comment|/** Whether the clock should initially be set to {@link java.time.Instant#EPOCH}. */
DECL|method|startAtEpoch ()
DECL|field|false
name|boolean
name|startAtEpoch
parameter_list|()
default|default
literal|false
function_decl|;
block|}
end_annotation_defn

end_unit

