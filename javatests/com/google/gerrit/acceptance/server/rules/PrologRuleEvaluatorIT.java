begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.server.rules
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|server
operator|.
name|rules
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|ImmutableList
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
name|acceptance
operator|.
name|AbstractDaemonTest
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
name|acceptance
operator|.
name|TestAccount
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
name|SubmitRecord
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
name|entities
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
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|ChangeData
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
name|rules
operator|.
name|PrologOptions
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
name|rules
operator|.
name|PrologRuleEvaluator
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
name|testing
operator|.
name|TestChanges
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
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|IntegerTerm
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|StructureTerm
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|Term
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
name|ObjectId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|PrologRuleEvaluatorIT
specifier|public
class|class
name|PrologRuleEvaluatorIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|evaluatorFactory
annotation|@
name|Inject
specifier|private
name|PrologRuleEvaluator
operator|.
name|Factory
name|evaluatorFactory
decl_stmt|;
annotation|@
name|Test
DECL|method|convertsPrologToSubmitRecord ()
specifier|public
name|void
name|convertsPrologToSubmitRecord
parameter_list|()
block|{
name|PrologRuleEvaluator
name|evaluator
init|=
name|makeEvaluator
argument_list|()
decl_stmt|;
name|StructureTerm
name|verifiedLabel
init|=
name|makeLabel
argument_list|(
literal|"Verified"
argument_list|,
literal|"may"
argument_list|)
decl_stmt|;
name|StructureTerm
name|labels
init|=
operator|new
name|StructureTerm
argument_list|(
literal|"label"
argument_list|,
name|verifiedLabel
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Term
argument_list|>
name|terms
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|makeTerm
argument_list|(
literal|"ok"
argument_list|,
name|labels
argument_list|)
argument_list|)
decl_stmt|;
name|SubmitRecord
name|record
init|=
name|evaluator
operator|.
name|resultsToSubmitRecord
argument_list|(
literal|null
argument_list|,
name|terms
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|record
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SubmitRecord
operator|.
name|Status
operator|.
name|OK
argument_list|)
expr_stmt|;
block|}
comment|/**    * The Prolog behavior is everything but intuitive. Several submit_rules can be defined, and each    * will provide a different SubmitRecord answer when queried. The current implementation stops    * parsing the Prolog terms into SubmitRecord objects once it finds an OK record. This might lead    * to tangling results, as reproduced by this test.    *    *<p>Let's consider this rules.pl file (equivalent to the code in this test)    *    *<pre>{@code    * submit_rule(submit(R)) :-    *     gerrit:uploader(U),    *     R = label('Verified', reject(U)).    *    * submit_rule(submit(CR, V)) :-    *     gerrit:uploader(U),    *     V = label('Code-Review', ok(U)).    *    * submit_rule(submit(R)) :-    *     gerrit:uploader(U),    *     R = label('Any-Label-Name', reject(U)).    * }</pre>    *    * The first submit_rule always fails because the Verified label is rejected.    *    *<p>The second submit_rule is always valid, and provides two labels: OK and Code-Review.    *    *<p>The third submit_rule always fails because the Any-Label-Name label is rejected.    *    *<p>In this case, the last two SubmitRecords are used, the first one is discarded.    */
annotation|@
name|Test
DECL|method|abortsEarlyWithOkayRecord ()
specifier|public
name|void
name|abortsEarlyWithOkayRecord
parameter_list|()
block|{
name|PrologRuleEvaluator
name|evaluator
init|=
name|makeEvaluator
argument_list|()
decl_stmt|;
name|SubmitRecord
operator|.
name|Label
name|submitRecordLabel1
init|=
operator|new
name|SubmitRecord
operator|.
name|Label
argument_list|()
decl_stmt|;
name|submitRecordLabel1
operator|.
name|label
operator|=
literal|"Verified"
expr_stmt|;
name|submitRecordLabel1
operator|.
name|status
operator|=
name|SubmitRecord
operator|.
name|Label
operator|.
name|Status
operator|.
name|REJECT
expr_stmt|;
name|submitRecordLabel1
operator|.
name|appliedBy
operator|=
name|admin
operator|.
name|id
argument_list|()
expr_stmt|;
name|SubmitRecord
operator|.
name|Label
name|submitRecordLabel2
init|=
operator|new
name|SubmitRecord
operator|.
name|Label
argument_list|()
decl_stmt|;
name|submitRecordLabel2
operator|.
name|label
operator|=
literal|"Code-Review"
expr_stmt|;
name|submitRecordLabel2
operator|.
name|status
operator|=
name|SubmitRecord
operator|.
name|Label
operator|.
name|Status
operator|.
name|OK
expr_stmt|;
name|submitRecordLabel2
operator|.
name|appliedBy
operator|=
name|admin
operator|.
name|id
argument_list|()
expr_stmt|;
name|SubmitRecord
operator|.
name|Label
name|submitRecordLabel3
init|=
operator|new
name|SubmitRecord
operator|.
name|Label
argument_list|()
decl_stmt|;
name|submitRecordLabel3
operator|.
name|label
operator|=
literal|"Any-Label-Name"
expr_stmt|;
name|submitRecordLabel3
operator|.
name|status
operator|=
name|SubmitRecord
operator|.
name|Label
operator|.
name|Status
operator|.
name|REJECT
expr_stmt|;
name|submitRecordLabel3
operator|.
name|appliedBy
operator|=
name|user
operator|.
name|id
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Term
argument_list|>
name|terms
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|StructureTerm
name|label1
init|=
name|makeLabel
argument_list|(
name|submitRecordLabel1
operator|.
name|label
argument_list|,
literal|"reject"
argument_list|,
name|admin
argument_list|)
decl_stmt|;
name|StructureTerm
name|label2
init|=
name|makeLabel
argument_list|(
name|submitRecordLabel2
operator|.
name|label
argument_list|,
literal|"ok"
argument_list|,
name|admin
argument_list|)
decl_stmt|;
name|StructureTerm
name|label3
init|=
name|makeLabel
argument_list|(
name|submitRecordLabel3
operator|.
name|label
argument_list|,
literal|"reject"
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|terms
operator|.
name|add
argument_list|(
name|makeTerm
argument_list|(
literal|"not_ready"
argument_list|,
name|makeLabels
argument_list|(
name|label1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|terms
operator|.
name|add
argument_list|(
name|makeTerm
argument_list|(
literal|"ok"
argument_list|,
name|makeLabels
argument_list|(
name|label2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|terms
operator|.
name|add
argument_list|(
name|makeTerm
argument_list|(
literal|"not_ready"
argument_list|,
name|makeLabels
argument_list|(
name|label3
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// When
name|SubmitRecord
name|record
init|=
name|evaluator
operator|.
name|resultsToSubmitRecord
argument_list|(
literal|null
argument_list|,
name|terms
argument_list|)
decl_stmt|;
comment|// assert that
name|SubmitRecord
name|expectedRecord
init|=
operator|new
name|SubmitRecord
argument_list|()
decl_stmt|;
name|expectedRecord
operator|.
name|status
operator|=
name|SubmitRecord
operator|.
name|Status
operator|.
name|OK
expr_stmt|;
name|expectedRecord
operator|.
name|labels
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|expectedRecord
operator|.
name|labels
operator|.
name|add
argument_list|(
name|submitRecordLabel2
argument_list|)
expr_stmt|;
name|expectedRecord
operator|.
name|labels
operator|.
name|add
argument_list|(
name|submitRecordLabel3
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|record
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedRecord
argument_list|)
expr_stmt|;
block|}
DECL|method|makeTerm (String status, StructureTerm labels)
specifier|private
specifier|static
name|Term
name|makeTerm
parameter_list|(
name|String
name|status
parameter_list|,
name|StructureTerm
name|labels
parameter_list|)
block|{
return|return
operator|new
name|StructureTerm
argument_list|(
name|status
argument_list|,
name|labels
argument_list|)
return|;
block|}
DECL|method|makeLabel (String name, String status)
specifier|private
specifier|static
name|StructureTerm
name|makeLabel
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|status
parameter_list|)
block|{
return|return
operator|new
name|StructureTerm
argument_list|(
literal|"label"
argument_list|,
operator|new
name|StructureTerm
argument_list|(
name|name
argument_list|)
argument_list|,
operator|new
name|StructureTerm
argument_list|(
name|status
argument_list|)
argument_list|)
return|;
block|}
DECL|method|makeLabel (String name, String status, TestAccount account)
specifier|private
specifier|static
name|StructureTerm
name|makeLabel
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|status
parameter_list|,
name|TestAccount
name|account
parameter_list|)
block|{
name|StructureTerm
name|user
init|=
operator|new
name|StructureTerm
argument_list|(
literal|"user"
argument_list|,
operator|new
name|IntegerTerm
argument_list|(
name|account
operator|.
name|id
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|new
name|StructureTerm
argument_list|(
literal|"label"
argument_list|,
operator|new
name|StructureTerm
argument_list|(
name|name
argument_list|)
argument_list|,
operator|new
name|StructureTerm
argument_list|(
name|status
argument_list|,
name|user
argument_list|)
argument_list|)
return|;
block|}
DECL|method|makeLabels (StructureTerm... labels)
specifier|private
specifier|static
name|StructureTerm
name|makeLabels
parameter_list|(
name|StructureTerm
modifier|...
name|labels
parameter_list|)
block|{
return|return
operator|new
name|StructureTerm
argument_list|(
literal|"label"
argument_list|,
name|labels
argument_list|)
return|;
block|}
DECL|method|makeChangeData ()
specifier|private
name|ChangeData
name|makeChangeData
parameter_list|()
block|{
name|ChangeData
name|cd
init|=
name|ChangeData
operator|.
name|createForTest
argument_list|(
name|project
argument_list|,
name|Change
operator|.
name|id
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|1
argument_list|,
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
decl_stmt|;
name|cd
operator|.
name|setChange
argument_list|(
name|TestChanges
operator|.
name|newChange
argument_list|(
name|project
argument_list|,
name|admin
operator|.
name|id
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|cd
return|;
block|}
DECL|method|makeEvaluator ()
specifier|private
name|PrologRuleEvaluator
name|makeEvaluator
parameter_list|()
block|{
return|return
name|evaluatorFactory
operator|.
name|create
argument_list|(
name|makeChangeData
argument_list|()
argument_list|,
name|PrologOptions
operator|.
name|defaultOptions
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

